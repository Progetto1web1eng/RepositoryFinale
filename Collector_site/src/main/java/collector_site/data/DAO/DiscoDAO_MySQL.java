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
import collector_site.data.impl.Tipo;
import collector_site.data.model.Artista;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import collector_site.data.model.Traccia;
import collector_site.data.proxy.DiscoProxy;

// import riguardanti il framework
import collector_site.framework.data.DAO;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataItemProxy;
import collector_site.framework.data.DataLayer;

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




    public DiscoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            storeDisco = connection.prepareStatement("INSERT INTO disco (nomeDisco,barcode,IDgenere,genere,anno,etichetta) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            deleteDisco = connection.prepareStatement("DELETE FROM disco WHERE ID=?"); 
            updateDisco = connection.prepareStatement("UPDATE disco SET nomeDisco=?,barcode=?,IDgenere=?,genere=?,anno=?,etichetta=? WHERE ID=?");
            getDisco = connection.prepareStatement("SELECT * FROM disco WHERE ID=?");
            getDischi = connection.prepareStatement("SELECT ID AS IDdisco FROM disco");
            getDiscoByCollezione = connection.prepareStatement("SELECT * FROM racchiude WHERE IDcollezione=?");
            // DA COMPLETARE
            getDiscoByTraccia = connection.prepareStatement("SELECT * FROM collezione WHERE IDcollezionista=?");
            //stefano deve controllare
            getDischiByArtista = connection.prepareStatement("SELECT * FROM incide WHERE IDartista=?");
            //stefano deve controllare
            getDischiByNome = connection.prepareStatement("SELECT * FROM disco WHERE nomeDisco=?");
            // query che modificano le quantità di Disco
            updateQuantitaDisco = connection.prepareStatement("UPDATE colleziona SET numCopieDisco=? WHERE ID=?");

            
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
            // disco.setTipo(Tipo.values()[rs.getInt("IDtipo")]);
            disco.setAnno(rs.getInt("anno"));
            disco.setEtichetta(rs.getString("etichetta"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create Disco object form ResultSet", ex);
        }
        return disco;
    }

    @Override
    public void updateDisco(String nomeDisco, String barcode, int anno, String etichetta, Genere genere, Collezionista collezionista, List<Artista> compositori, List<Immagine> immagini, List<Traccia> tracce) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteDisco(Disco disco) {
        
        if (disco.getKey() == null || disco.getKey() >= 0) {
            return;
        }
        
        try {
            if (deleteDisco.executeUpdate() == 0) {
                System.out.println("l'eliminazione del disco non è andata a buon fine");
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
                result.add((Disco) getDisco(rs.getInt("ID")));
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
       
        try {
            
            // CREAZIONE DISCO
            if ("".equals(disco.getNomeDisco()) ||
                    disco.getGenere() == null ||
                    disco.getAnno() == 0  ||      
                    "".equals(disco.getEtichetta())) {
                return;
            }
            
            storeDisco.setString(1, disco.getNomeDisco());
            
            if ("".equals(disco.getBarcode())) {
                storeDisco.setNull(2, java.sql.Types.CHAR);
            } else {
                storeDisco.setString(2, disco.getBarcode());
            }
            
            String genere = disco.getGenere().toString();
            
            storeDisco.setInt(3, Genere.valueOf(genere).ordinal());
            storeDisco.setString(4, genere);
            storeDisco.setInt(5, disco.getAnno());
            storeDisco.setString(6, disco.getEtichetta());
            
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
            
            // questo "if" deve essere eseguto sia quando si fa la create che l'update della Collezione 
            if (disco instanceof DataItemProxy) {
                ((DataItemProxy) disco).setModified(false);
            }
           
        } catch (SQLException ex) {
            throw new DataException("Unable to store Disco", ex);
        }
    }
        

    @Override
    public void updateQuantitaDisco(Disco disco, int quantitaDisco) throws DataException {
        
        
        
        
        
        
        
        
    }
    
    //aggiunto per fare il push di artistaProxy (DA COMPLETARE LUNEDI)
    @Override
    public List<Disco> getDischiIncisi() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
     
}