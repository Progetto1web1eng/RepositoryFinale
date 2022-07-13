/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.DAO;

import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import collector_site.data.proxy.CollezioneProxy;

// import riguardanti il framework
import collector_site.framework.data.DAO;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataItemProxy;
import collector_site.framework.data.DataLayer;
import collector_site.framework.data.OptimisticLockException;
import static java.lang.System.out;

// import SQL
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// import Java
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stefa
 */
public class CollezioneDAO_MySQL extends DAO implements CollezioneDAO {

    private PreparedStatement createCollezione;
    private PreparedStatement deleteCollezione;
    private PreparedStatement updateCollezione;
    private PreparedStatement updateCondivisione;
    private PreparedStatement getCollezioneById;
    private PreparedStatement getCollezioneByCollezionista;
    private PreparedStatement getCollezioneByDisco;
    private PreparedStatement getCollezioneByBarcodeDisco;
    private PreparedStatement getCollezioneByNomeDisco; 
    private PreparedStatement storeCollezione;
    private PreparedStatement getCondivisione;
    private PreparedStatement addCondivisione;
    private PreparedStatement deleteCondivisione;
    private PreparedStatement getCollezioniCondiviseToCollezionista;
    private PreparedStatement getCollezioniPubbliche;
    private PreparedStatement getCollezioniAccessibiliLoggato;
    private PreparedStatement getCollezioniPubblicheByCollezionista;
    private PreparedStatement getCondivisioniByCollezione;

    public CollezioneDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            storeCollezione = connection.prepareStatement("INSERT INTO collezione (nomeCollezione,pubblico,IDcollezionista) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            deleteCollezione = connection.prepareStatement("DELETE FROM collezione WHERE ID=?"); 
            updateCollezione = connection.prepareStatement("UPDATE collezione SET nomeCollezione=?,pubblico=? WHERE ID=?");
            getCollezioneById = connection.prepareStatement("SELECT * FROM collezione WHERE ID=?");
            getCollezioneByCollezionista = connection.prepareStatement("SELECT * FROM collezione WHERE IDcollezionista=?");
            getCollezioneByDisco = connection.prepareStatement("SELECT c.ID, c.nomeCollezione, c.IDcollezionista, c.pubblico FROM collezione c join racchiude r on(c.ID = r.IDcollezione) WHERE (r.IDdisco = ?);");
            getCollezioneByBarcodeDisco = connection.prepareStatement("SELECT c.ID, c.nomeCollezione, c.IDcollezionista, c.pubblico FROM collezione c join racchiude r join disco d on(r.IDdisco = d.ID and c.ID = r.IDcollezione) WHERE (d.barcode = ?)");
            getCollezioneByNomeDisco = connection.prepareStatement("SELECT c.ID, c.nomeCollezione, c.IDcollezionista, c.pubblico FROM collezione c join racchiude r join disco d on(r.IDdisco = d.ID and c.ID = r.IDcollezione) WHERE (d.nomeDisco = ?)");
            // query che operano sulla tabella Condivide
            getCondivisione = connection.prepareStatement("SELECT count(*) as count FROM condivide WHERE IDcollezionista=? and IDcollezione=?"); 
            addCondivisione = connection.prepareStatement("INSERT INTO condivide (IDcollezionista,IDcollezione) VALUES(?,?)");
            deleteCondivisione = connection.prepareStatement("DELETE FROM condivide WHERE IDcollezionista=? and IDcollezione=?"); 
            getCollezioniCondiviseToCollezionista = connection.prepareStatement("SELECT IDcollezione FROM condivide WHERE IDcollezionista =?");
            getCollezioniAccessibiliLoggato = connection.prepareStatement("select con.IDcollezionista as collezionista_loggato, col.IDcollezionista as collezionista_target, col.ID as IDcollezione from condivide con join collezione col on(con.IDcollezione = col.ID) where (con.IDcollezionista=? and col.IDcollezionista=? and col.pubblico = false);");   
            getCollezioniPubblicheByCollezionista = connection.prepareStatement("select c.ID from collezione c where c.IDcollezionista=? and c.pubblico=true;");  
            getCollezioniPubbliche = connection.prepareStatement("SELECT * FROM collezione WHERE pubblico=true");
            getCondivisioniByCollezione = connection.prepareStatement("SELECT IDcollezionista FROM condivide WHERE IDcollezione =?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing Collezione data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            createCollezione.close();
            deleteCollezione.close();
            updateCollezione.close();
            getCollezioneById.close();
            getCollezioneByCollezionista.close();
            getCollezioneByDisco.close();
            getCollezioneByBarcodeDisco.close();
            getCollezioneByNomeDisco.close();
            getCondivisione.close();
            addCondivisione.close();
            deleteCondivisione.close();
            storeCollezione.close();
            getCollezioniCondiviseToCollezionista.close();
            getCollezioniPubbliche.close();
            getCollezioniAccessibiliLoggato.close();
            getCollezioniPubblicheByCollezionista.close();
            getCondivisioniByCollezione.close();
        } catch (SQLException ex) {
        }
        super.destroy();
    }

    @Override
    public CollezioneProxy createCollezione(ResultSet rs) throws DataException {
        CollezioneProxy collezione = (CollezioneProxy) createCollezione();
        try {
           collezione.setKey(rs.getInt("ID"));
           collezione.setNomeCollezione(rs.getString("nomeCollezione"));
           collezione.setPubblico(rs.getBoolean("pubblico"));
           collezione.setCollezionistaKey(rs.getInt("IDcollezionista")); 
        } catch (SQLException ex) {
            throw new DataException("Unable to create Collezione object form ResultSet", ex);
        }
        return collezione;
    }
    
    @Override
    public Collezione createCollezione() {
        return new CollezioneProxy(getDataLayer());
    }

    @Override
    public void deleteCollezione(Collezione collezione) {
        
        if (collezione.getKey() == null || collezione.getKey() <= 0) {
            return;
        }
        
        try {
            deleteCollezione.setInt(1, collezione.getKey());
            if (deleteCollezione.executeUpdate() == 0) {
                // qui si deve sollevare eccezione
            }
        } catch (SQLException ex) {
            Logger.getLogger(CollezioneDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void updateCondivisione(Collezionista collezionista) throws collector_site.framework.data.DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Collezione getCollezioneById(int id) throws collector_site.framework.data.DataException {
         Collezione collezione = null;
         
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Collezione.class, id)) {
            // caso in cui la Collezione è già presente nella CACHE
            collezione = dataLayer.getCache().get(Collezione.class, id);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                getCollezioneById.setInt(1,id);
                try (ResultSet rs = getCollezioneById.executeQuery()) {
                    if (rs.next()) {
                        collezione = createCollezione(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the caches
                        dataLayer.getCache().add(Collezione.class, collezione);
                         
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Collezione by its ID", ex);
            }
        }
        return collezione;
        
         
    }
    

    @Override
    public List<Collezione> getCollezioneByCollezionista(Collezionista collezionista) throws collector_site.framework.data.DataException {
        // questo metodo restituisce tutte le collezioni create dal Collezionista in questione
        List<Collezione> listaCollezioni = new ArrayList();
        try {
            getCollezioneByCollezionista.setInt(1, collezionista.getKey());
            try (ResultSet rs = getCollezioneByCollezionista.executeQuery()) {
                while (rs.next()) {
                    listaCollezioni.add(getCollezioneById(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezioni by Collezionista", ex);
        }
        return listaCollezioni;
    }
    
    

    @Override
    public List<Collezione> getCollezioneByDisco(Disco disco) throws collector_site.framework.data.DataException {
        List<Collezione> result = new ArrayList();
        try {
            getCollezioneByDisco.setInt(1, disco.getKey());
            try (ResultSet rs = getCollezioneByDisco.executeQuery()) {
                while (rs.next()) {
                    result.add(getCollezioneById(rs.getInt("c.ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezione by Disco", ex);
        }
        return result;
    }

    @Override
    public List<Collezione> getCollezioneByBarcodeDisco(String barcode) throws collector_site.framework.data.DataException {
        List<Collezione> result = new ArrayList();
        try {
            getCollezioneByBarcodeDisco.setString(1, barcode);
            try (ResultSet rs = getCollezioneByBarcodeDisco.executeQuery()) {
                while (rs.next()) {
                    result.add(getCollezioneById(rs.getInt("c.ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezione by BarcodeDisco", ex);
        }
        return result;
    }

    @Override
    public List<Collezione> getCollezioneByNomeDisco(String nomeDisco) throws collector_site.framework.data.DataException {
        List<Collezione> result = new ArrayList();
        try {
            getCollezioneByNomeDisco.setString(1, nomeDisco);
            try (ResultSet rs = getCollezioneByNomeDisco.executeQuery()) {
                while (rs.next()) {
                    result.add(getCollezioneById(rs.getInt("IDc")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezione by nomeDisco", ex);
        }
        return result;
    }
    
    
    @Override
    public void storeCollezione(Collezione collezione)throws collector_site.framework.data.DataException{
        
       try {
            
            // CREAZIONE COLLEZIONE
            
            // se si inserisce un nome di Collezione non valido, non viene effettutata la creazizone della Collezione
            if (collezione.getNomeCollezione() == null ||
                    "".equals(collezione.getNomeCollezione()) ||
                    collezione.getCreatore().getKey() == null ||
                    collezione.getCreatore().getKey() <= 0) {
                return;
            }
            
            // controllo che uno stesso collezionista non possa creare più di una collezione con il medesimo
            // nome
            for(Collezione c : getCollezioneByCollezionista(collezione.getCreatore())) {
                if(collezione.getNomeCollezione().equals(c.getNomeCollezione())) {
                    return; // si deve sollevare ECCEZIONE
                }
            }
            
            storeCollezione.setString(1, collezione.getNomeCollezione());
            storeCollezione.setBoolean(2, collezione.getPubblico());
            storeCollezione.setInt(3, collezione.getCreatore().getKey());
            
            if (storeCollezione.executeUpdate() == 1) {
                //per leggere la chiave generata dal database per il record appena inserito
                try (ResultSet keys = storeCollezione.getGeneratedKeys()) {
                       
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            //aggiornaimo la chiave anche nell oggetto di Collezione
                            collezione.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            dataLayer.getCache().add(Collezione.class, collezione);
                        }
                    }
                }
            
            // questo "if" deve essere eseguto sia quando si fa la create che l'update della Collezione 
            if (collezione instanceof DataItemProxy) {
                ((DataItemProxy) collezione).setModified(false);
            }
           
        } catch (SQLException ex) {
            throw new DataException("Unable to store article", ex);
        }
            
        /*
        try{
            if(collezione.getKey() != null && collezione.getKey()>0) {
                //check se l'oggetto è un proxy e non ha modifiche
                if(collezione instanceof DataItemProxy && !((DataItemProxy)collezione).isModified()){
                    return;
                }
            
            updateCollezione.setString(1, collezione.getNomeCollezione());
            updateCollezione.setBoolean(2, collezione.getPubblico());
            if(collezione.getCreatore() != null){
            updateCollezione.setInt(3, collezione.getCreatore().getKey());
        }else{
                updateCollezione.setNull(3, java.sql.Types.INTEGER);
            }
            }else{
                createCollezione.setString(1, collezione.getNomeCollezione());
                createCollezione.setBoolean(2, collezione.getPubblico());
                if(collezione.getCreatore() != null){
                    createCollezione.setInt(3, collezione.getCreatore().getKey());
                }
            
            }
            if(collezione instanceof DataItemProxy){
                ((DataItemProxy)collezione).setModified(false);
            }
    }catch (SQLException ex){
        throw new DataException("Unable to store collezione",ex);*/
        
}

    @Override
    public void addCondivisione(Collezione collezione, Collezionista collezionista) throws DataException {
        // con questo metodo si condivide la presente Collezione con il Collezionista in questione
        
        // non dovremmo preoccuparci di CONCURRENCY per questo metodo perché la gestione delle condivisioni è
        // personale ==> la modifica di una condivisione 
        
        // controllo validità ID della Collezione
        if (collezione.getKey() == null || collezione.getKey() <= 0) {
            return;
        }
        
        // controllo validità ID del Collezionista
        if (collezionista.getKey() == null || collezionista.getKey() <= 0) {
            return;
        }
        
        // controllo sul tipo di Collezione
        if(collezione.getPubblico() == true) {
            // caso in cui la collezione in questione è pubblica ==> non è necessario aggiungere condivisioni
            // a quest'ultimo
            return;
        }
        
        try {
            getCondivisione.setInt(1, collezionista.getKey());
            getCondivisione.setInt(2, collezione.getKey());

            try (ResultSet rs = getCondivisione.executeQuery()) {
                // controllo se la Collezione in questione risulta già condiviso con il presente Collezionista 
                if (rs.next()) {
                    if(rs.getInt("count") == 0) {
                        addCondivisione.setInt(1, collezionista.getKey());
                        addCondivisione.setInt(2, collezione.getKey());
                        // si aggiunge una tupla alla tabella Condivide
                        if (addCondivisione.executeUpdate() == 0) {
                            // solleva eccezione
                        } 
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile aggiungere la condivsione", ex);
        }
    }

    @Override
    public void deleteCondivisione(Collezione collezione, Collezionista collezionista) throws DataException {
        // per non rendere più accessibile la presente Collezione al Collezionista in questione
        
        // controllo validità ID della Collezione
        if (collezione.getKey() == null || collezione.getKey() <= 0) {
            return;
        }
        
        // controllo validità ID del Collezionista
        if (collezionista.getKey() == null || collezionista.getKey() <= 0) {
            return;
        }
        
        // controllo sul tipo di Collezione
        if(collezione.getPubblico() == true) {
            // caso in cui la collezione in questione è pubblica ==> non è necessario rimuovere condivisioni
            // a quest'ultimo
            return;
        }
        
        try {
            deleteCondivisione.setInt(1, collezionista.getKey());
            deleteCondivisione.setInt(2, collezione.getKey());
            
            if (deleteCondivisione.executeUpdate() == 0) {
                // solleva eccezione
            }
        } catch (SQLException ex) {
            Logger.getLogger(CollezioneDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Collezione> getCollezioniCondiviseToCollezionista(Collezionista collezionista) throws DataException {
        List<Collezione> listaCollezioni = new ArrayList();
        
        // aggiungo le collezioni non pubbliche condivise al Collezionista in questione a "listaCollezioni"
        try {
            getCollezioniCondiviseToCollezionista.setInt(1, collezionista.getKey());
            try (ResultSet rs = getCollezioniCondiviseToCollezionista.executeQuery()) {
                while (rs.next()) {
                    listaCollezioni.add(getCollezioneById(rs.getInt("IDcollezione")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezioni condivise by Collezionista", ex);
        }
        
        // aggiungo le collezioni  a "listaCollezioni"
        try {
            try (ResultSet rs = getCollezioniPubbliche.executeQuery()) {
                while (rs.next()) {
                    listaCollezioni.add(getCollezioneById(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezioni condivise by Collezionista", ex);
        }
        return listaCollezioni;
        
        /*
        List<Collezione> listaCollezioni = new ArrayList();
        
        // aggiungo le collezioni non pubbliche condivise al Collezionista in questione a "listaCollezioni"
        try {
            getCollezioniCondiviseToCollezionista.setInt(1, collezionista.getKey());
            try (ResultSet rs = getCollezioniCondiviseToCollezionista.executeQuery()) {
                while (rs.next()) {
                    listaCollezioni.add(getCollezioneById(rs.getInt("IDcollezione")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezioni condivise by Collezionista", ex);
        }
        
        // aggiungo le collezioni pubbliche a "listaCollezioni"
        try {
            try (ResultSet rs = getCollezioniPubbliche.executeQuery()) {
                while (rs.next()) {
                    listaCollezioni.add(getCollezioneById(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezioni condivise by Collezionista", ex);
        }
        return listaCollezioni;
        
        */
        
        
    }

    @Override
    public List<Collezione> getCollezioniPrivateCondiviseToCollezionista(Collezionista collezionista) throws DataException {
        List<Collezione> listaCollezioni = new ArrayList();
        
        // restituisce le collezioni non pubbliche condivise al Collezionista in questione
        try {
            getCollezioniCondiviseToCollezionista.setInt(1, collezionista.getKey());
            try (ResultSet rs = getCollezioniCondiviseToCollezionista.executeQuery()) {
                while (rs.next()) {
                    listaCollezioni.add(getCollezioneById(rs.getInt("IDcollezione")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezioni condivise by Collezionista", ex);
        }
        
        return listaCollezioni;
    }

    @Override
    public List<Collezione> getCollezioniAccessibili(Collezionista collezionista_target, Collezionista collezionista_loggato) throws DataException {
        List<Collezione> result = new ArrayList();
        
        try {
            getCollezioniPubblicheByCollezionista.setInt(1, collezionista_target.getKey());
            
            try (ResultSet rs = getCollezioniPubblicheByCollezionista.executeQuery()) {
                while (rs.next()) {
                    // si aggiungono nella lista soltanto le collezioni pubbliche create dal collezionista_target
                    result.add(getCollezioneById(rs.getInt("c.ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezioni by Collezionista", ex);
        }
        
        if(collezionista_loggato == null) {
            // caso in cui l'utente non è LOGGATO ==> l'utente potrà visualizzare soltanto le collezioni pubbliche 
            // che ha creato il collezionista target
            return result;
        }
        
        
        try {
            getCollezioniAccessibiliLoggato.setInt(1, collezionista_loggato.getKey());
            getCollezioniAccessibiliLoggato.setInt(2, collezionista_target.getKey());

            try (ResultSet rs = getCollezioniAccessibiliLoggato.executeQuery()) {
                while (rs.next()) {
                    // nella lista si aggiungono solo le collezioni private create dal collezionista_target 
                    // che sono state condivise al collezionista_loggato
                    result.add(getCollezioneById(rs.getInt("IDcollezione")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezioni by Collezionista", ex);
        }
        
        return result;
    }

    @Override
    public List<Collezione> getCollezioniAccessibiliByNome(String nomeCollezione, Collezionista collezionista) throws DataException {
        List<Collezione> result = new ArrayList();
        // per eseguire una ricerca case insensitive
        nomeCollezione = nomeCollezione.toUpperCase();
        
        try {
            
            try (ResultSet rs = getCollezioniPubbliche.executeQuery()) {
                while (rs.next()) {
                    // si aggiungono nella lista soltanto le collezioni pubbliche presenti nella webapp
                    // che hanno il nome indicato
                    if(nomeCollezione.equals(rs.getString("nomeCollezione").toUpperCase())) {
                        result.add(getCollezioneById(rs.getInt("ID")));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezioni pubbliche in getCollezioniAccessibiliByNome", ex);
        }
        
        if(collezionista == null) {
            // caso in cui l'utente non è LOGGATO ==> l'utente potrà visualizzare soltanto le collezioni 
            // pubbliche presenti nella webapp 
            return result;
        }
        
        // nella lista si aggiungono solo le collezioni private che sono state condivise al collezionista in
        // questione che hanno il nome indicato
        for(Collezione c : getCollezioniPrivateCondiviseToCollezionista(collezionista)) {
            if(nomeCollezione.equals(c.getNomeCollezione().toUpperCase())) {
                result.add(c);
            }
        }
        
        return result;
    }
    
}
