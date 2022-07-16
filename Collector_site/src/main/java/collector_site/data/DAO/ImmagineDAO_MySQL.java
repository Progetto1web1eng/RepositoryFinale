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
import collector_site.data.model.Collezione;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import collector_site.data.proxy.CollezionistaProxy;
import collector_site.data.proxy.DiscoProxy;
import collector_site.data.proxy.ImmagineProxy;

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
 * @author stefa
 */
public class ImmagineDAO_MySQL extends DAO implements ImmagineDAO {

    private PreparedStatement createImmagine;
    private PreparedStatement deleteImmagine;
    private PreparedStatement getImmagineById;
    private PreparedStatement getImmagineByDisco;
    private PreparedStatement storeImmagine;

    public ImmagineDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            
            createImmagine = connection.prepareStatement("INSERT INTO immagine (nomeImmagine,dimensioneImmagine,filename,imgType,IDdisco) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            deleteImmagine = connection.prepareStatement("DELETE FROM immagine WHERE ID=?"); 
            getImmagineById = connection.prepareStatement("SELECT * FROM immagine WHERE ID=?");
            getImmagineByDisco = connection.prepareStatement("SELECT * FROM immagine WHERE IDdisco=?");
            storeImmagine = connection.prepareStatement("insert into immagine(nomeImmagine,dimensioneImmagine,filename,imgType,IDdisco,digest,updated) VALUES(?,?,?,?,?,?,CURRENT_TIMESTAMP);", new String[]{"ID"}); 
        } catch (SQLException ex) {
            throw new DataException("Error initializing Immagine data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            createImmagine.close();
            deleteImmagine.close();
            getImmagineById.close();
            getImmagineByDisco.close();
            storeImmagine.close();
        } catch (SQLException ex) {
        }
        super.destroy();
    }

    @Override
    public Immagine createImmagine() {
        return new ImmagineProxy(getDataLayer());
    }
    
    private ImmagineProxy createImmagine(ResultSet rs) throws DataException {
        ImmagineProxy immagine = (ImmagineProxy) createImmagine();
        
        try {
            immagine.setKey(rs.getInt("ID"));
            immagine.setNomeImmagine(rs.getString("nomeImmagine"));
            immagine.setDimensioneImmagine((long) rs.getInt("dimensioneImmagine"));
            immagine.setFilename(rs.getString("filename"));
            immagine.setImgType(rs.getString("imgType"));
            immagine.setDiscoKey(rs.getInt("IDdisco"));
            immagine.setDigest(rs.getString("digest"));
            immagine.setUpdated(rs.getTimestamp("updated"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create Immagine object form ResultSet", ex);
        }
        return immagine;
    }

    @Override
    public void deleteImmagine(Immagine immagine) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Immagine getImmagineById(int id) throws collector_site.framework.data.DataException {
        Immagine immagine = null;
        //prima vediamo se l'oggetto è già stato caricato
        if (dataLayer.getCache().has(Immagine.class, id)) {
            immagine = dataLayer.getCache().get(Immagine.class, id);
        } else {
            //altrimenti lo carichiamo dal database
            try {
                getImmagineById.setInt(1, id);
                
                try (ResultSet rs = getImmagineById.executeQuery()) {
                    if (rs.next()) {
                        immagine = createImmagine(rs);
                        
                        //e lo mettiamo anche nella cache
                        dataLayer.getCache().add(Immagine.class, immagine);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Immagine by ID", ex);
            }
        }
        
        return immagine;
    }

    @Override
    public List<Immagine> getImmaginiByDisco(Disco disco) throws collector_site.framework.data.DataException {
        List<Immagine> result = new ArrayList();
        
        try {
            getImmagineByDisco.setInt(1, disco.getKey());
            
            try (ResultSet rs = getImmagineByDisco.executeQuery()) {
                while (rs.next()) {
                    result.add(getImmagineById(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Immagine by Disco", ex);
        }
        
        return result;
    }

    @Override
    public List<Immagine> getImmagini() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void storeImmagine(Immagine immagine) throws DataException {
          
            try {
            // il controllo della validità del formato dell'immagine che si vuole salvare avviene già nella
            // servlet
            
            storeImmagine.setString(1, immagine.getNomeImmagine());
            storeImmagine.setInt(2, (int) immagine.getDimensioneImmagine());
            storeImmagine.setString(3, immagine.getFilename());
            storeImmagine.setString(4, immagine.getImgType());
            storeImmagine.setInt(5, immagine.getDiscoImg().getKey());
            storeImmagine.setString(6, immagine.getDigest());
            
            if (storeImmagine.executeUpdate() == 1) {
                try ( //estrae l'ID dell'immagine caricata
                    ResultSet keys = storeImmagine.getGeneratedKeys()) {
                    keys.first();
                    immagine.setKey(keys.getInt(1));
                    //inseriamo il nuovo oggetto nella cache
                    dataLayer.getCache().add(Immagine.class, immagine);
                }
               
            }

            // questo "if" deve essere eseguto sia quando si fa la create che l'update della Collezione 
            if (immagine instanceof DataItemProxy) {
                ((DataItemProxy) immagine).setModified(false); 
            }
           
        } catch (SQLException ex) {
            throw new DataException("Unable to store Immagine", ex);
        } 
            
        // REMOVE 
        System.out.println("ultima riga storeImmagine");
    }
}
