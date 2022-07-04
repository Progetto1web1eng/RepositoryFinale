/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.DAO;

/**
 *
 * @author stefa
 */
import collector_site.data.impl.Genere;
import collector_site.data.impl.Ruolo;
import collector_site.data.impl.Tipo;
import collector_site.data.model.Artista;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import collector_site.data.model.Traccia;
import collector_site.data.proxy.DiscoProxy;
import collector_site.data.impl.CopieStato;
import collector_site.data.impl.StatoDisco;


// import riguardanti il framework
import collector_site.framework.data.DAO;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataItemProxy;
import collector_site.framework.data.DataLayer;
import static java.lang.System.out;

// import SQL
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// import Java
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stefa
 */
public class DiscoDAO_MySQL extends DAO implements DiscoDao {

    private PreparedStatement storeDisco;
    private PreparedStatement deleteDisco;
    private PreparedStatement updateDisco;
    private PreparedStatement getDisco;
    private PreparedStatement getDischi;
    private PreparedStatement getDiscoByCollezione;
    private PreparedStatement getDiscoByTraccia;
    private PreparedStatement getDischiByArtista;
    private PreparedStatement getDischiByNome;
    private PreparedStatement increaseQuantitaDisco;
    private PreparedStatement updateQuantitaDisco;
    private PreparedStatement addDiscoToCollezione;
    private PreparedStatement removeDiscoFromCollezione;
    private PreparedStatement setArtistaOfDisco;
    private PreparedStatement storeQuantitaDisco;
    private PreparedStatement getQuantitaDisco;
    private PreparedStatement getStatiDischi;
    private PreparedStatement getDischiByCollezionista;
    private PreparedStatement getDischiByGenere;

    public DiscoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            storeDisco = connection.prepareStatement("INSERT INTO disco (nomeDisco,barcode,IDgenere,genere,anno,etichetta,IDtipo,tipo) VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            deleteDisco = connection.prepareStatement("DELETE FROM disco WHERE ID=?"); 
            updateDisco = connection.prepareStatement("UPDATE disco SET nomeDisco=?,barcode=?,IDgenere=?,genere=?,anno=?,etichetta=?,IDtipo=?,tipo=? WHERE ID=?");
            getDisco = connection.prepareStatement("SELECT * FROM disco WHERE ID=?");
            getDischi = connection.prepareStatement("SELECT ID AS IDdisco FROM disco");
            getDiscoByCollezione = connection.prepareStatement("SELECT * FROM racchiude WHERE IDcollezione=?");
            // DA COMPLETARE
            getDiscoByTraccia = connection.prepareStatement("SELECT * FROM collezione WHERE IDcollezionista=?");
            //stefano deve controllare
            getDischiByArtista = connection.prepareStatement("SELECT * FROM incide WHERE IDartista=?");
            //stefano deve controllare
            getDischiByNome = connection.prepareStatement("SELECT * FROM disco WHERE nomeDisco=?");
            addDiscoToCollezione = connection.prepareStatement("INSERT INTO racchiude (IDcollezione,IDdisco) VALUES(?,?)"); 
            removeDiscoFromCollezione = connection.prepareStatement("DELETE FROM racchiude WHERE IDcollezione=? and IDdisco=?;");
            setArtistaOfDisco = connection.prepareStatement("INSERT INTO incide (IDdisco,IDartista) VALUES(?,?)"); 
            
            // query che manipolano le quantità dei dischi
            updateQuantitaDisco = connection.prepareStatement("UPDATE colleziona SET numCopieDisco=? WHERE IDcollezionista=? and IDdisco=? and IDstatoDisco=?");
            storeQuantitaDisco = connection.prepareStatement("INSERT INTO colleziona (numCopieDisco,IDstatoDisco,statoDisco,IDcollezionista,IDdisco) VALUES(?,?,?,?,?)");
            getQuantitaDisco = connection.prepareStatement("SELECT count(*) as count FROM colleziona WHERE IDcollezionista=? and IDdisco=? and IDstatoDisco=?;");
            getStatiDischi = connection.prepareStatement("SELECT nome FROM statoDisco");
            getDischiByCollezionista = connection.prepareStatement("SELECT c.IDdisco FROM colleziona c WHERE (c.IDcollezionista=?);");
            getDischiByGenere = connection.prepareStatement("SELECT * FROM disco d where (d.IDgenere = ?);");
        } catch (SQLException ex) {
            throw new DataException("Error initializing Disco data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            storeDisco.close();
            deleteDisco.close();
            updateDisco.close();
            getDisco.close();
            getDischi.close();
            getDiscoByCollezione.close();
            getDiscoByTraccia.close();
            updateQuantitaDisco.close();
            addDiscoToCollezione.close();
            removeDiscoFromCollezione.close();
            setArtistaOfDisco.close();
            storeQuantitaDisco.close();
            getQuantitaDisco.close();
            getStatiDischi.close();
            getDischiByCollezionista.close();
        } catch (SQLException ex) {
        }
        super.destroy();
    }

    @Override
    public Disco createDisco() {
        return new DiscoProxy(getDataLayer());
    }
    
    // $
    @Override
    public DiscoProxy createDisco(ResultSet rs) throws DataException {
        DiscoProxy disco = (DiscoProxy) createDisco();
        try {
            disco.setKey(rs.getInt("ID"));
            disco.setNomeDisco(rs.getString("nomeDisco"));
            disco.setBarcode(rs.getString("barcode"));
            // Genere è un ENUMERAZIONE
            disco.setGenere(Genere.values()[rs.getInt("IDgenere")]);
            // Tipo è un enumerazione
            disco.setTipo(Tipo.values()[rs.getInt("IDtipo")]);
            disco.setAnno(rs.getInt("anno"));
            disco.setEtichetta(rs.getString("etichetta"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create Disco object form ResultSet", ex);
        }
        return disco;
    }

    @Override
    public void deleteDisco(Disco disco) {
        
        if (disco.getKey() == null || disco.getKey() >= 0) {
            return;
        }
        
        try {
            if (deleteDisco.executeUpdate() == 0) {
                // qui si deve sollevare eccezione
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiscoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Disco getDisco(int id) throws collector_site.framework.data.DataException {
        Disco disco = null;
        
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Disco.class, id)) {
            disco = dataLayer.getCache().get(Disco.class, id);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                getDisco.setInt(1, id);
                try (ResultSet rs = getDisco.executeQuery()) {
                    if (rs.next()) {
                        disco = createDisco(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Disco.class, disco);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Disco by ID", ex);
            }
        }
        return disco;
        
        
    }

    @Override
    public List<Disco> getDischi() throws collector_site.framework.data.DataException {
        List<Disco> result = new ArrayList();

        try (ResultSet rs = getDischi.executeQuery()) {
            while (rs.next()) {
                result.add((Disco) getDisco(rs.getInt("IDdisco")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Dischi", ex);
        }
        return result;
    }

    // $
    @Override
    public List<Disco> getDiscoByCollezione(Collezione collezione) throws collector_site.framework.data.DataException {
        List<Disco> result = new ArrayList();
        
        try {
            getDiscoByCollezione.setInt(1, collezione.getKey());
            try (ResultSet rs = getDiscoByCollezione.executeQuery()) {
                while (rs.next()) {
                    result.add(getDisco(rs.getInt("IDdisco")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Disco by Collezione", ex);
        }
        return result;
    }

    @Override
    public Disco getDiscoByTraccia(Traccia traccia) throws collector_site.framework.data.DataException {
        Disco disco = null;
        
        try {
            getDiscoByTraccia.setInt(1, traccia.getKey());
            try (ResultSet rs = getDiscoByTraccia.executeQuery()) {
                if (rs.next()) {
                    disco = getDisco(rs.getInt("d.ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Disco by Traccia", ex);
        }
        return disco;
    }
    
    // CHECKED by Stefano
    @Override
    public List<Disco> getDischiByArtista(Artista artista) throws DataException {
        List<Disco> result = new ArrayList();
        
        try{
            getDischiByArtista.setInt(2, artista.getKey());
            try(ResultSet rs = getDischiByArtista.executeQuery()){
                while (rs.next()){
                    result.add(getDisco(rs.getInt("IDdisco")));
                }
            }
        }catch(SQLException ex){
            throw new DataException("Unable to load Dischi by Artista");
        }
        return result;
    }
    
    // CHECKED by Stefano
    @Override
    public List<Disco> getDischiByNome(String nome)throws DataException{
        List<Disco> result = new ArrayList();
        
        try{
            getDischiByNome.setString(2, nome);
            try(ResultSet rs = getDischiByNome.executeQuery()){
                result.add(getDisco(rs.getInt("ID")));
            }
            
        }catch(SQLException ex){
            throw new DataException("Unable to load Disco by nome");
        }
        return result;
    }
    
    @Override
    public void storeDisco (Disco disco)throws DataException{
        
        if ("".equals(disco.getNomeDisco()) ||
                    disco.getGenere() == null ||
                    disco.getAnno() == 0  ||      
                    disco.getTipo() == null ||
                    "".equals(disco.getEtichetta())) {
                return;
        }
        
        try {
            if (disco.getKey() != null && disco.getKey() > 0) {
                // UPDATE DISCO
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                if (disco instanceof DataItemProxy && !((DataItemProxy) disco).isModified()) {
                    return;
                }
            
                updateDisco.setString(1, disco.getNomeDisco());
            
                if ("".equals(disco.getBarcode())) {
                    storeDisco.setNull(2, java.sql.Types.CHAR);
                } else {
                    storeDisco.setString(2, disco.getBarcode());
                }

                String genere = disco.getGenere().toString();
            
                storeDisco.setInt(3, Genere.valueOf(genere).ordinal() + 1);
                storeDisco.setString(4, genere);
                storeDisco.setInt(5, disco.getAnno());
                storeDisco.setString(6, disco.getEtichetta());
            
                String tipo =  disco.getTipo().toString(); 
                storeDisco.setInt(7, Tipo.valueOf(tipo).ordinal() + 1);
                storeDisco.setString(8, tipo);
            
                long current_version = disco.getVersion();
                long next_version = current_version + 1;

                if (updateDisco.executeUpdate() == 0) {
                    // CHECK
                    // throw new OptimisticLockException(article);
                    // solleva eccezione
                }

            } else {
            
                // CREAZIONE DISCO
                storeDisco.setString(1, disco.getNomeDisco());
            
                if ("".equals(disco.getBarcode())) {
                    storeDisco.setNull(2, java.sql.Types.CHAR);
                } else {
                    storeDisco.setString(2, disco.getBarcode());
                }
            
                String genere = disco.getGenere().toString();
            
                storeDisco.setInt(3, Genere.valueOf(genere).ordinal() + 1);
                storeDisco.setString(4, genere);
                storeDisco.setInt(5, disco.getAnno());
                storeDisco.setString(6, disco.getEtichetta());
            
                String tipo =  disco.getTipo().toString(); 
                // MODIFICATO
                storeDisco.setInt(7, Tipo.valueOf(tipo).ordinal() + 1);
                storeDisco.setString(8, tipo);
            
                if (storeDisco.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database per il record appena inserito
                    try (ResultSet keys = storeDisco.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            //aggiornaimo la chiave anche nell oggetto di Collezione
                            disco.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            dataLayer.getCache().add(Disco.class, disco);
                        }
                    }
                }
            }
                
        // questo "if" deve essere eseguto sia quando si fa la create che l'update della Collezione 
        if (disco instanceof DataItemProxy) {
            ((DataItemProxy) disco).setModified(false);
        }
        } catch (SQLException ex) {
            throw new DataException("Unable to store Disco", ex);
        }
    }

        

    @Override
    public void updateQuantitaDisco(Disco disco, Collezionista collezionista, StatoDisco statoDisco, int nuovaQuantita) throws DataException {
           
        if (nuovaQuantita < 0) {
                return; //solleva eccezione
        }
        
         try {
        
            updateQuantitaDisco.setInt(1, nuovaQuantita);
            
            updateQuantitaDisco.setInt(2, collezionista.getKey());
            updateQuantitaDisco.setInt(3, disco.getKey());
            updateQuantitaDisco.setInt(4, StatoDisco.valueOf(statoDisco.toString()).ordinal() + 1);
           
            if (storeQuantitaDisco.executeUpdate() != 1) {
                // solleva eccezione
            }

        } catch (SQLException ex) {
            throw new DataException("Unable to update quantità del disco", ex);
        }
    }

    @Override
    public void removeDiscoToCollezione(Disco disco, Collezione collezione) throws DataException {
         try {
           
            removeDiscoFromCollezione.setInt(1, collezione.getKey());
            removeDiscoFromCollezione.setInt(2, disco.getKey());

            if (removeDiscoFromCollezione.executeUpdate() != 1) {
                // solleva eccezione
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to remove Disco from Collezione", ex);
        }
    }    

    @Override
    public void setArtistaOfDisco(Disco disco, Artista artista) throws DataException {
        // in questo metodo è possibile passare come valore del parametro "artista" un gruppo musicale oppure
        // un'Artista
        
        try {
            
            setArtistaOfDisco.setInt(1, disco.getKey());
            setArtistaOfDisco.setInt(2, artista.getKey());
            
            if (setArtistaOfDisco.executeUpdate() != 1) {
                //solleva eccezione
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to set Artista of Disco", ex);
        }
    }

    @Override
    public void addDiscoToCollezionista(Disco disco, Collezionista collezionista) throws DataException {
        
        // controllo che evita l'inserimento di tuple duplicate nella tabella Colleziona
        String[] statiDischi = new String[2];
        statiDischi[0] = "Nuovo";
        statiDischi[1] = "Usato";
        
        for (CopieStato c : disco.getCopieStati()) {
            CopieStato cs = null;
            
            for(int i=0;i<=statiDischi.length;i++) {
                if(c.getStato().toString().equals(statiDischi[i])) {
                    cs = c;
                    
                    try{
                        getQuantitaDisco.setInt(1, collezionista.getKey());
                        getQuantitaDisco.setInt(2, disco.getKey());
                        getQuantitaDisco.setInt(3, StatoDisco.valueOf(statiDischi[i]).ordinal() + 1);
                        
                        try (ResultSet rs = getQuantitaDisco.executeQuery()) {
                            if (rs.next()) {
                                if(rs.getInt("count") == 0) {
                                        // caso in cui la tupla non è già presente nella tabella "colleziona"
                                        storeQuantitaDisco.setInt(1, cs.getNumCopieDisco());
            
                                        storeQuantitaDisco.setInt(2, StatoDisco.valueOf(statiDischi[i]).ordinal() + 1);
                                        storeQuantitaDisco.setString(3, statiDischi[i]);
        
                                        storeQuantitaDisco.setInt(4, collezionista.getKey());
                                        storeQuantitaDisco.setInt(5, disco.getKey());
                        
                                        if (storeQuantitaDisco.executeUpdate() != 1) {
                                                // solleva eccezione
                                        }
                                    }           
                                }
                            }
                        } catch (SQLException ex) {
                            throw new DataException("Unable to add Disco to Collezionista", ex);
                        }
                }
            }
        }
    }
        
        
      
    @Override
    public void addDiscoToCollezione(Disco disco, Collezione collezione) throws DataException {
        
        try {
            getDiscoByCollezione.setInt(1, collezione.getKey());

            try (ResultSet rs = getDiscoByCollezione.executeQuery()) {
                while (rs.next()) {
                    if (rs.getInt("IDdisco") == disco.getKey()) {
                        // caso in cui il Disco in questione è stato già associato alla collezione
                        return;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Disco by Collezione", ex);
        }
        
        try {
            
            addDiscoToCollezione.setInt(1, collezione.getKey());
            addDiscoToCollezione.setInt(2, disco.getKey());
 
            if (addDiscoToCollezione.executeUpdate() != 1) {
                // solleva eccezione
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to add Disco to Collezione", ex);
        }
    }

    @Override
    public List<Disco> getDischiIncisi() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Disco> getDischiByNome(String nomeDisco, Collezionista collezionista) throws DataException {
        List<Disco> result = new ArrayList();
        out.println(collezionista.getUsername());
        List<Disco> dl = getDischiByCollezionista(collezionista);
        out.println("ho creato dl");
        for(Disco disco : dl) {
            out.println("ciclo dentro i dischi del collezionista, disco: "+disco.getNomeDisco());
            if(nomeDisco.equals(disco.getNomeDisco())) {
                result.add(disco);
            }
        }
        return result; 
    }

    @Override
    public List<Disco> getDischiByCollezionista(Collezionista collezionista) throws DataException {
        List<Disco> result = new ArrayList();
        
        try{
            getDischiByCollezionista.setInt(1, collezionista.getKey());
            out.println("dentro detDischiByCollezionista"+collezionista.getKey());
            
            try(ResultSet rs = getDischiByCollezionista.executeQuery()){
                while(rs.next()) {
                    result.add(getDisco(rs.getInt("c.IDdisco")));
                }
            }
            
        }catch(SQLException ex){
            throw new DataException("Unable to load Disco by Collezionista");
        }
        return result;
    }

    @Override
    public List<Disco> getDischiByArtista(Artista artista, Collezionista collezionista) throws DataException {
        List<Disco> result = new ArrayList<>();
        
        for(Disco d : getDischiByCollezionista(collezionista)) {
            
            for(Artista a : d.getCompositori()) {
                if(artista.getKey().equals(a.getKey())) {
                    result.add(d);
                }
            }
        }
        
        return result;
    }

    @Override
    public List<Disco> getDischiByGenere(Genere genere) throws DataException {
        List<Disco> result = new ArrayList();
        
        try{
            Integer idGenere = Genere.valueOf(genere.toString()).ordinal() + 1;
            getDischiByGenere.setInt(1, idGenere);
            
            try(ResultSet rs = getDischiByGenere.executeQuery()){
                result.add(getDisco(rs.getInt("d.ID")));
            }
            
        }catch(SQLException ex){
            throw new DataException("Unable to load Disco by Genere");
        }
        return result;
    }

    @Override
    public List<Disco> getDischiByGenere(Genere genere, Collezionista collezionista) throws DataException {
        List<Disco> result = new ArrayList<>();
        
        for(Disco disco : getDischiByCollezionista(collezionista)) {
            // contiene l'ID del genere per il quale si effettua la ricerca tra i dischi
            Integer idGenere1 = Genere.valueOf(genere.toString()).ordinal() + 1; 
            Integer idGenere2 = Genere.valueOf(disco.getGenere().toString()).ordinal() + 1; 

            if(idGenere1.intValue() == idGenere2.intValue() ) {
                result.add(disco);
            }   
        }
        return result;
    }

}