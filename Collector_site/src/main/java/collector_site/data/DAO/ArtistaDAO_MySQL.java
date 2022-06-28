/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author stefa
 */

package collector_site.data.DAO;

// AGGIUNGERE IMPORT: PROXY

import collector_site.data.impl.Genere;
import collector_site.data.impl.Ruolo;
import collector_site.data.impl.Tipo;
import collector_site.framework.data.DAO;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataLayer;
import collector_site.data.model.Artista;
import collector_site.data.model.Disco;
import collector_site.data.proxy.ArtistaProxy;
import collector_site.data.proxy.DiscoProxy;

// import riguardanti il framework
import collector_site.framework.data.DataItemProxy;
import collector_site.framework.data.OptimisticLockException;

// import SQL
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// import Java
import java.util.ArrayList;
import java.util.List;


public class ArtistaDAO_MySQL extends DAO implements ArtistaDao {

    private PreparedStatement storeArtista;
    private PreparedStatement deleteArtista;
    private PreparedStatement getArtistaById;
    private PreparedStatement getArtisti;

    public ArtistaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            storeArtista = connection.prepareStatement("INSERT INTO artista (nomeDarte,IDruolo,ruolo,IDgruppoMusicale) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            deleteArtista = connection.prepareStatement("DELETE FROM artista WHERE ID=?"); 
            getArtistaById = connection.prepareStatement("SELECT * FROM artista WHERE ID=?");
            getArtisti = connection.prepareStatement("SELECT ID AS IDartista FROM artista");
        } catch (SQLException ex) {
            throw new DataException("Error initializing Artista data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            storeArtista.close();
            deleteArtista.close();
            getArtistaById.close();
            getArtisti.close();
        } catch (SQLException ex) {
        }
        super.destroy();
    }

    @Override
    public Artista createArtista() {
        return new ArtistaProxy(getDataLayer());  
    }
    
    // $
    @Override
    public ArtistaProxy createArtista(ResultSet rs) throws DataException {
        ArtistaProxy artista = (ArtistaProxy) createArtista();
        
        try {
            artista.setKey(rs.getInt("ID"));
            artista.setNomeDarte(rs.getString("nomeDarte"));
            // Ruolo è un enumerazione
            artista.setRuolo(Ruolo.values()[rs.getInt("IDruolo")]);
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create Artista object form ResultSet", ex);
        }
        return artista;
    }

    @Override
    public void deleteArtista(Artista artista) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Artista getArtistaById(int id) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Artista> getArtisti(int artista_key) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Artista> getCompositori() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void storeArtista(Artista artista) throws DataException {
        /*
        try {
            
            // CREAZIONE DISCO
            if ("".equals(artista.getNomeDarte())) {
                return;
            }
            
            storeArtista.setString(2, artista.getNomeDarte());
            
            // per inserire i valori degli attributi IDruolo e ruolo nella tabella Artista
            if (artista.getRuolo() == null) {
                // IDruolo
                storeArtista.setNull(3, java.sql.Types.SMALLINT);
                // ruolo
                storeArtista.setNull(4, java.sql.Types.VARCHAR);
            } else {
                String ruolo = artista.getRuolo().toString();
                // IDruolo
                storeArtista.setInt(3, Ruolo.valueOf(ruolo).ordinal());
                // ruolo
                storeArtista.setString(4, ruolo);
            }
            
            // per inserire il valore dell'attributo IDgruppoMusicale nella tabella Artista
            if (artista.getGruppoMusicale() == null) {
                storeArtista.setNull(5, java.sql.Types.SMALLINT);
            } else {
                storeArtista.setInt(5, artista.getGruppoMusicale().getKey());
            }
            
            if (storeArtista.executeUpdate() == 1) {
                //per leggere la chiave generata dal database per il record appena inserito
                try (ResultSet keys = storeArtista.getGeneratedKeys()) {
                       
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            //aggiornaimo la chiave anche nell oggetto di Collezione
                            artista.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            dataLayer.getCache().add(Artista.class, artista);
                        }
                    }
                }
            
            // questo "if" deve essere eseguto sia quando si fa la create che l'update dell'Artista 
            if (artista instanceof DataItemProxy) {
                ((DataItemProxy) artista).setModified(false);
            }
           
        } catch (SQLException ex) {
            throw new DataException("Unable to store Artista", ex);
        }*/
    } 
}


