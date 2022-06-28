/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.DAO;

/**
 *
 * @author stefa
 */
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import collector_site.data.proxy.CollezionistaProxy;
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
            immagine.setDimensioneImmagine(rs.getLong("dimensioneImmagine"));
            immagine.setFilename(rs.getString("filename"));
            immagine.setImgType(rs.getString("imgType"));
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
        //first look for this object in the cache
        if (dataLayer.getCache().has(Immagine.class, id)) {
            immagine = dataLayer.getCache().get(Immagine.class, id);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                getImmagineById.setInt(1, id);
                try (ResultSet rs = getImmagineById.executeQuery()) {
                    if (rs.next()) {
                        immagine = createImmagine(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
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
                    result.add(getImmagineById(rs.getInt("IDdisco")));
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

   
}
