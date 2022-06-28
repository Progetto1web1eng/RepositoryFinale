/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.DAO;

import collector_site.data.impl.CopieStato;
import collector_site.data.model.Artista;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import collector_site.data.model.Traccia;
import collector_site.framework.data.DataException;
import java.sql.SQLException;
import javax.sql.DataSource;
import collector_site.framework.data.DataLayer;

/**
 *
 * @author fabri
 */
public class Collector_siteDataLayer extends DataLayer {
    
    public Collector_siteDataLayer(DataSource datasource) throws SQLException, DataException {
        super(datasource);
        
    }

    @Override
    public void init() throws DataException {
        //registriamo i nostri dao
        //register our daos
        registerDAO(Artista.class, new ArtistaDAO_MySQL(this));
        registerDAO(Collezione.class, new CollezioneDAO_MySQL(this));
        registerDAO(Collezionista.class, new CollezionistaDAO_MySQL(this));
        registerDAO(CopieStato.class, new CopieStatoDAO_MySQL(this));
        registerDAO(Disco.class, new DiscoDAO_MySQL(this));
        registerDAO(Immagine.class, new ImmagineDAO_MySQL(this));
        registerDAO(Traccia.class, new TracciaDAO_MySQL(this));
        
    }

    //helpers    
    public ArtistaDao getArtistaDAO() {
        return (ArtistaDao) getDAO(Artista.class);
    }

    public CollezioneDAO getCollezioneDAO() {
        return (CollezioneDAO) getDAO(Collezione.class);
    }

    public CollezionistaDAO getCollezionistaDAO() {
        return (CollezionistaDAO) getDAO(Collezionista.class);
    }

    public CopieStatoDAO getCopieStatoDAO() {
        return (CopieStatoDAO) getDAO(CopieStato.class);
    }
    public DiscoDao getDiscoDAO() {
        return (DiscoDao) getDAO(Disco.class);
    }
     public ImmagineDAO getImmagineDAO() {
        return (ImmagineDAO) getDAO(Immagine.class);
    }
     public TracciaDAO getTracciaDAO() {
        return (TracciaDAO) getDAO(Traccia.class);
    }
}
