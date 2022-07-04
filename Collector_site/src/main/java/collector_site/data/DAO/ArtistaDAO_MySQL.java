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
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.proxy.ArtistaProxy;
import collector_site.data.proxy.DiscoProxy;

// import riguardanti il framework
import collector_site.framework.data.DataItemProxy;
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


public class ArtistaDAO_MySQL extends DAO implements ArtistaDao {

    private PreparedStatement storeArtista;
    private PreparedStatement deleteArtista;
    private PreparedStatement getArtistaById;
    private PreparedStatement getArtistaByNomeDarte;
    private PreparedStatement getArtisti;
    private PreparedStatement getArtistaByDisco;
    private PreparedStatement getArtistiByGruppoMusicale;
    private PreparedStatement getArtistiPreferiti;

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
            getArtistaByDisco = connection.prepareStatement("SELECT IDartista FROM incide WHERE IDdisco=?");
            getArtistiByGruppoMusicale = connection.prepareStatement("SELECT * FROM artista WHERE IDgruppoMusicale=?");
            getArtistiPreferiti = connection.prepareStatement("SELECT count(i.IDartista), i.IDartista FROM colleziona c join incide i on(c.IDdisco = i.IDdisco) WHERE (c.IDcollezionista = ?) GROUP BY i.IDartista ORDER BY count(i.IDartista) desc;");
            getArtistaByNomeDarte = connection.prepareStatement("SELECT * FROM artista WHERE nomeDarte=? LIMIT 1");

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
            getArtistaByDisco.close();
            getArtistiByGruppoMusicale.close();
            getArtistiPreferiti.close();
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
            System.out.println(rs.getInt("ID"));
            System.out.println(rs.getString("nomeDarte"));
            // Ruolo è un enumerazione
            // CHECK
            // artista.setRuolo(Ruolo.values()[rs.getInt("IDruolo") - 1]);
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
        Artista artista = null;
         
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Artista.class, id)) {
            // caso in cui la Collezione è già presente nella CACHE
            artista = dataLayer.getCache().get(Artista.class, id);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                getArtistaById.setInt(1,id);
                
                int IDgruppoMusicale = 0;
                try (ResultSet rs = getArtistaById.executeQuery()) {
                    if (rs.next()) {
                        // IDgruppoMusicale = rs.getInt("IDgruppoMusicale"); 
                        
                        artista = createArtista(rs);
                        out.println(artista.getKey());
                        out.println(artista.getNomeDarte());
                        
                        artista = getArtistiByGruppoMusicale(artista);
                        
                        //out.println(artista.getComponenti().get(0).getNomeDarte());

                        
                        /*
                        if (rs.wasNull()) {
                            // caso in cui l'artista in questione è un gruppo musicale oppure un singolo 
                            // Artista
                            artista = createArtista(rs); 
                            
        
                        } */
                    }
                }
            
            } catch (SQLException ex) {
                throw new DataException("Unable to load Collezione by its ID", ex);
            }
        }
        //e lo mettiamo anche nella cache
        dataLayer.getCache().add(Artista.class, artista);
        
        return artista;
        
        
        
        
        
        
        
        
        /*
        Artista artista = null;
        
        //prima vediamo se l'oggetto è già stato caricato
        if (dataLayer.getCache().has(Artista.class, id)) {
            artista = dataLayer.getCache().get(Artista.class, id);
        } else {
            //altrimenti lo carichiamo dal database
            try {
                getArtistaById.setInt(1, id);
                try (ResultSet rs = getArtistaById.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("prima createArtista");
                        artista = createArtista(rs);
                        System.out.println("dopo createArtista");
                        // si controlla se l'artista in questione è un gruppo musicale e se sì, si aggiungono
                        // i suoi componenti
                        artista = getArtistiByGruppoMusicale(artista);
                        //e lo mettiamo anche nella cache
                        dataLayer.getCache().add(Artista.class, artista);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Artista by ID", ex);
            }
        }
        return artista; */
    }

    
    @Override
    public Artista getArtistaNomeDarte(String nomeDarte) throws DataException {
        Artista artista = null;
            try {
                getArtistaByNomeDarte.setString(1, nomeDarte);
                try (ResultSet rs = getArtistaByNomeDarte.executeQuery()) {
                    if (rs.next()) {
                        artista = createArtista(rs);
                        // si controlla se l'artista in questione è un gruppo musicale e se sì, si aggiungono
                        // i suoi componenti
                        artista = getArtistiByGruppoMusicale(artista);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Artista by ID", ex);
            }
        return artista;
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
        // si assume che il sistema permetta soltanto lo store e non l'update dell'Artista
        
        try {
            
            // REMOVE
            System.out.println(artista.getNomeDarte());
            
            if ("".equals(artista.getNomeDarte())) {
                return;  
            }
            
            storeArtista.setString(1, artista.getNomeDarte());
             // inserimento valore nullo nell'attributo IDRuolo in tabella Artista
            storeArtista.setNull(2, java.sql.Types.SMALLINT);
            // inserimento valore nullo nell'attributo ruolo in tabella Artista
            storeArtista.setNull(3, java.sql.Types.VARCHAR);
            // inserimento valore nullo nell'attributo IDgruppoMusicale in tabella Artista
            storeArtista.setNull(4, java.sql.Types.SMALLINT);
            
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
        }
        
        if (artista.getComponenti() != null && artista.getComponenti().size() > 1) {
            // caso in cui si fa lo store di un gruppo musicale
                
            for (Artista a : artista.getComponenti()) {
                // store di ciascun componente del gruppo musicale
                Integer idGruppoMusicale = artista.getKey();
                storeComponenteGruppo(a, idGruppoMusicale);
                    
                } 
            }   
    } 

    @Override
    public Artista getArtistaByDisco(Disco disco) throws DataException {
        Artista artista = null;
        
        try {
            getArtistaByDisco.setInt(1, disco.getKey());
            try (ResultSet rs = getArtistaByDisco.executeQuery()) {
                if (rs.next()) {
                    artista = getArtistaById(rs.getInt("IDartista"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Artista by Disco", ex);
        }
        return artista;
    }

    @Override
    public Artista getArtistiByGruppoMusicale(Artista gruppoMusicale) throws DataException {
        List<Artista> componenti = new ArrayList<Artista>();
        
        try {
            getArtistiByGruppoMusicale.setInt(1, gruppoMusicale.getKey() );
            
            try (ResultSet rs = getArtistiByGruppoMusicale.executeQuery()) {
                while (rs.next()) {
                    Artista componente = null;
                    
                    componente = createArtista(rs);
                    
                    componente.setRuolo(Ruolo.values()[rs.getInt("IDruolo") - 1]);
                    
                    componenti.add(componente);
                }
                
                if(componenti.size() > 0) {
                    gruppoMusicale.setComponenti(componenti);
                    
                    return gruppoMusicale;
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Artista by Disco", ex);
        }
        
        return gruppoMusicale;
        
        /*
        
        List<Artista> componenti = new ArrayList<Artista>();
        
        try{
            getArtistiByGruppoMusicale.setInt(1, artista.getKey());
            try(ResultSet rs = getArtistiByGruppoMusicale.executeQuery()){
                while (rs.next()){
                    Artista componente = getArtistaById(rs.getInt("ID"));

                    // CHECK
                    componente.setRuolo(Ruolo.values()[rs.getInt("IDruolo") - 1]);
                    componenti.add(componente);
                }
            }
        }catch(SQLException ex){
            throw new DataException("Unable to load Artista by gruppo musicale");
        }
        
        if (!componenti.isEmpty()) {
            // caso in cui l'oggetto {O} passato come parametro è un gruppo musicale ==> è necessario 
            // aggiungere ad {0} i componenti del gruppo musicale
            artista.setComponenti(componenti);
            return artista;
        }
        
        return artista; // caso in cui l'oggetto passato come parametro non è un gruppo musicale ==> non è 
                        // necessario aggiungere ad {0} i componenti del gruppo musicale in quanto {o} 
                        // rappresenta un singolo artista e non un gruppo musicale
*/
    }

    @Override
    public void storeComponenteGruppo(Artista artista, Integer idGruppoMusicale) throws DataException {
        try {
            
            // REMOVE
            System.out.println("store di un componente");
            // si dovrebbero fare più controlli
            if ("".equals(artista.getNomeDarte())) {
                return;
            }
            
            storeArtista.setString(1, artista.getNomeDarte());
            
            String ruolo = artista.getRuolo().toString();
            
            storeArtista.setInt(2, Ruolo.valueOf(ruolo).ordinal() + 1);
            storeArtista.setString(3, ruolo);
            
            // estraggo l'ID del gruppo musicale al quale appartiene il componente in questione e lo inserisco
            // nel record che si sta creando
            storeArtista.setInt(4, idGruppoMusicale);
            
            //REMOVE 
            System.out.println(artista.getComponenti().get(0).getKey());
            
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
            
            // questo "if" deve essere eseguto sia quando si fa la create che l'update della Collezione 
            if (artista instanceof DataItemProxy) {
                ((DataItemProxy) artista).setModified(false);
            }
           
        } catch (SQLException ex) {
            throw new DataException("Unable to store Artista", ex);
        }
    }
    
    @Override
    public List<Artista> getArtistiPreferiti(Collezionista collezionista) throws DataException {
        // in questa lista verrano salvati gli ID degli artisti preferiti
        List<Artista> result = new ArrayList<Artista>();
        
        try{
            getArtistiPreferiti.setInt(1, collezionista.getKey());
            
            try(ResultSet rs = getArtistiPreferiti.executeQuery()){
                int count = 0;
                
                while (rs.next()){
                    System.out.println("entra nel while");
                    result.add(getArtistaById(rs.getInt("i.IDartista")));
                    System.out.println("dopo getArtistaById");
                    count++;
                    
                    if(count >= 3) {
                        break;
                    }
                }
            }
        }catch(SQLException ex){
            throw new DataException("Unable to load artisti preferiti");
        }
        
        return result;
    }
}


