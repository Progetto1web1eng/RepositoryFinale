/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.DAO;

/**
 *
 * @author stefa
 */
import collector_site.data.model.Artista;
import collector_site.data.model.Collezione;
import collector_site.data.model.Disco;
import collector_site.data.model.Traccia;
import collector_site.data.proxy.TracciaProxy;

// import riguardanti il framework
import collector_site.framework.data.DAO;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataItemProxy;
import collector_site.framework.data.DataLayer;
import collector_site.framework.data.OptimisticLockException;

// import SQL
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// import Java
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mauri
 */
public class TracciaDAO_MySQL extends DAO implements TracciaDAO {

    private PreparedStatement createTraccia;
    private PreparedStatement deleteTraccia;
    private PreparedStatement getTracciaById;
    private PreparedStatement getTracciaByArtista;
    private PreparedStatement getTracceByDisco;
    private PreparedStatement storeTraccia;


    public TracciaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            createTraccia = connection.prepareStatement("INSERT INTO traccia (titolo,durata,IDdisco) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            deleteTraccia = connection.prepareStatement("DELETE FROM traccia WHERE ID=?"); 
            getTracciaById = connection.prepareStatement("SELECT * FROM traccia WHERE ID=?");
            getTracciaByArtista = connection.prepareStatement("SELECT * FROM crea WHERE IDartista=?");
            //se non funziona l'errore è nella select del FORM "traccia"
            getTracceByDisco = connection.prepareStatement("SELECT * FROM traccia WHERE IDdisco=?");
            storeTraccia = connection.prepareStatement("INSERT INTO traccia (titolo,durata,IDdisco) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new DataException("Error initializing Traccia data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            createTraccia.close();
            deleteTraccia.close();
            getTracceByDisco.close();
            getTracciaById.close();
            getTracciaByArtista.close();
        } catch (SQLException ex) {
        }
        super.destroy();
    }

    @Override
    public Traccia createTraccia() {
        return new TracciaProxy(getDataLayer());
    }
    @Override
    public TracciaProxy createTraccia(ResultSet rs)throws DataException {
        TracciaProxy traccia = (TracciaProxy) createTraccia();
        try{
            traccia.setKey(rs.getInt("ID"));
            traccia.setTitolo(rs.getString("titolo"));
            traccia.setDurata(rs.getTime("durata"));
            traccia.setDiscoKey(rs.getInt("IDdisco"));
        }catch(SQLException ex){
            throw new DataException("unable to create Traccia object form ResultSet",ex);
        }
        return traccia;
    }

    @Override
    public void deleteTraccia(Traccia traccia) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    // fino a qui tutto controllato
    
    
    
    @Override
    public Traccia getTracciaById(int traccia_key) throws DataException {
        Traccia t = null;
        //check se l'articolo è già stato caricato
        if(dataLayer.getCache().has(Traccia.class,traccia_key)){
            t = dataLayer.getCache().get(Traccia.class, traccia_key);
        }else{
            //carichiamo l'oggetto dal DB
            try{
                getTracciaById.setInt(1, traccia_key);
                try(ResultSet rs = getTracciaById.executeQuery()){
                    if(rs.next()){
                        //creiamo l'oggetto
                        t = createTraccia(rs);
                        //lo inseriamo nella nella cache
                        dataLayer.getCache().add(Traccia.class, t);       
                    }
                }
            }catch(SQLException ex){
                throw new DataException("Unable to load traccia by ID");
            }
        }
        return t;
    }

    //QUESTO E? DA LEVAREEEEEEEEEEEE?!????????!!!!?!?!?!?!?!?,noi non selezioniamo una lista di tracce dall'artista,ma da un disco
    @Override
    public List<Traccia> getTracciaByArtista(Artista artista) throws collector_site.framework.data.DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Traccia> getTracceByDisco(Disco disco) throws DataException {
        List<Traccia> result = new ArrayList();
        try{
            getTracceByDisco.setInt(1, disco.getKey());
            try(ResultSet rs = getTracceByDisco.executeQuery()){
                while (rs.next()){
                    result.add(getTracciaById(rs.getInt("ID")));
                }
            }
        }catch(SQLException ex){
            throw new DataException("Unable to load Tracce by Disco");
        }
        return result;
    }

    @Override
    public void storeTraccia(Traccia traccia) throws DataException {
        
        // CREAZIONE TRACCIA
        if (traccia.getDurata() == null ||
            "".equals(traccia.getTitolo()) ||
            traccia.getDisco().getKey() == null ||
            traccia.getDisco().getKey() <= 0) {
               
            return;
        }
        
        try {
            
            storeTraccia.setString(1, traccia.getTitolo());
            // CHANGED
            //storeTraccia.setTime(2, traccia.getDurata());
            storeTraccia.setTime(2, new java.sql.Time(traccia.getDurata().getTime()));
            
                System.out.println("arrivato");
            storeTraccia.setInt(3, traccia.getDisco().getKey());
            
            if (storeTraccia.executeUpdate() == 1) {
                //per leggere la chiave generata dal database per il record appena inserito
                try (ResultSet keys = storeTraccia.getGeneratedKeys()) {
                       
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            //aggiornaimo la chiave anche nell oggetto di Collezione
                            traccia.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            dataLayer.getCache().add(Traccia.class, traccia);
                        }
                    }
                }
            
            // questo "if" deve essere eseguto sia quando si fa la create che l'update della Collezione 
            if (traccia instanceof DataItemProxy) {
                ((DataItemProxy) traccia).setModified(false);
            }
           
        } catch (SQLException ex) {
            throw new DataException("Unable to store Traccia", ex);
        }
    
    
    
    
    }
    
}