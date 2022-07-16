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
import collector_site.data.model.Artista;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.proxy.CollezionistaProxy;

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
public class CollezionistaDAO_MySQL extends DAO implements CollezionistaDAO {

    private PreparedStatement storeCollezionista;
    private PreparedStatement deleteCollezionista;
    private PreparedStatement updateCollezionista;
    private PreparedStatement getCollezionistaById;
    private PreparedStatement getCollezionistaByCollezione;
    private PreparedStatement getCollezionistaByUandP;
    private PreparedStatement getCollezionisti;
    private PreparedStatement getCollezionistaByNickname;
    private PreparedStatement getGenerePreferito;
    private PreparedStatement getCondivisioniByCollezione;
    private PreparedStatement login;
    private PreparedStatement getCollezionistaByDisco;
   
    public CollezionistaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            storeCollezionista = connection.prepareStatement("INSERT INTO collezionista (nickname,email,username,password,cellulare) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            deleteCollezionista = connection.prepareStatement("DELETE FROM collezionista WHERE ID=?"); 
            updateCollezionista = connection.prepareStatement("UPDATE collezionista SET nickname=?,email=?,username=?,password=?,cellulare=? WHERE ID=?");
            getCollezionistaById = connection.prepareStatement("SELECT * FROM collezionista WHERE ID=?");
            getCollezionistaByCollezione = connection.prepareStatement("SELECT c.IDcollezionista FROM collezione c WHERE (c.ID = ?);");
            getCollezionistaByUandP = connection.prepareStatement("SELECT * FROM collezionista WHERE username=? AND password=?");
            getCollezionisti = connection.prepareStatement("SELECT ID FROM collezionista");
            getCollezionistaByNickname = connection.prepareStatement("SELECT ID FROM collezionista WHERE nickname=?");
            getGenerePreferito = connection.prepareStatement("SELECT count(d.IDgenere), d.IDgenere FROM colleziona c join disco d on(c.IDdisco = d.ID) WHERE (c.IDcollezionista =?) GROUP BY d.IDgenere ORDER BY count(d.IDgenere) desc;");
            getCondivisioniByCollezione = connection.prepareStatement("SELECT IDcollezionista FROM condivide WHERE IDcollezione =?");
            login = connection.prepareStatement("SELECT ID FROM collezionista WHERE nickname=? and password=?");
            getCollezionistaByDisco = connection.prepareStatement("select c.IDcollezionista from colleziona c where c.IDdisco=? limit 1;");
        } catch (SQLException ex) {
            throw new DataException("Error initializing Collezionista data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            storeCollezionista.close();
            deleteCollezionista.close();
            updateCollezionista.close();
            getCollezionistaById.close();
            getCollezionistaByCollezione.close();
            getCollezionistaByUandP.close();
            getCollezionisti.close();
            getCollezionistaByNickname.close();
            getGenerePreferito.close();
            getCondivisioniByCollezione.close();
            login.close();
            getCollezionistaByDisco.close();
        } catch (SQLException ex) {
        }
        super.destroy();
    }

     private CollezionistaProxy createCollezionista(ResultSet rs) throws DataException {
        CollezionistaProxy collezionista = (CollezionistaProxy) createCollezionista();
        try {
            collezionista.setKey(rs.getInt("ID"));
            collezionista.setNickname(rs.getString("nickname"));
            collezionista.setEmail(rs.getString("email"));
            collezionista.setUsername(rs.getString("username"));
            collezionista.setPassword(rs.getString("password"));
            collezionista.setCellulare(rs.getString("cellulare"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create Collezionista object form ResultSet", ex);
        }
        return collezionista;
    }
    
    @Override
    public Collezionista createCollezionista() {
        return new CollezionistaProxy(getDataLayer());
    }

    @Override
    public void delteCollezionista(Collezionista collezionista) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void updateCollezionista(String nickname, int cellulare) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Collezionista getCollezionistaById(int id) throws collector_site.framework.data.DataException {
        Collezionista collezionista = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Collezionista.class, id)) {
            collezionista = dataLayer.getCache().get(Collezionista.class, id);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                getCollezionistaById.setInt(1, id);
                try (ResultSet rs = getCollezionistaById.executeQuery()) {
                    if (rs.next()) {
                        collezionista = createCollezionista(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Collezionista.class, collezionista);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Collezionista by ID", ex);
            }
        }
        return collezionista;
    }

   @Override
    public Collezionista getCollezionistaByCollezione(Collezione collezione) throws DataException{
        Collezionista collezionista = null;
        
        try {
            getCollezionistaByCollezione.setInt(1, collezione.getKey());
            
            try (ResultSet rs = getCollezionistaByCollezione.executeQuery()) {
                if (rs.next()) {
                    collezionista = getCollezionistaById(rs.getInt("c.IDcollezionista")); 
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezionista by Collezione", ex);
        }
        
        return collezionista;
    }     
    
    @Override
    public List<Collezionista> getCollezionisti() throws collector_site.framework.data.DataException {
        List<Collezionista> result = new ArrayList();

        try (ResultSet rs = getCollezionisti.executeQuery()) {
            while (rs.next()) {
                result.add((Collezionista) getCollezionistaById(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load collezionisti", ex);
        }
        return result;
    }

    @Override
    public Collezionista getCreatore(int collezionista_key) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    @Override
    public Collezionista getCollezionistaByUandP(String username, String password){
         throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Artista> getCompositori(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void storeCollezionista(Collezionista collezionista) {
    try {
            
            // CREAZIONE COLLEZIONISTA
            // controllo che il nicnkame, l'email, lo username e la password che si inseriscono non siano
            // nulli
            if ("".equals(collezionista.getNickname()) ||
                    "".equals(collezionista.getEmail()) ||
                    // "".equals(collezionista.getUsername()) ||
                    "".equals(collezionista.getPassword())) {
                return;
            }
            
            // controllo univocità di nickname, email e cellulare
            for(Collezionista c : getCollezionisti()) {
                // controllo univocità del nickname
                if(c.getNickname().equals(collezionista.getNickname())) {
                    return; // sollevare ECCEZIONE
                }
                
                // controllo univocità dell'email
                if(c.getEmail().equals(collezionista.getEmail())) {
                    return; // sollevare eccezione
                }
                
                // controllo univocità del cellulare
                if(c.getCellulare().equals(collezionista.getCellulare())) {
                    return; // sollevare eccezione
                }
            }
            
            storeCollezionista.setString(1, collezionista.getNickname());
            storeCollezionista.setString(2, collezionista.getEmail());
            storeCollezionista.setString(3, "");
            storeCollezionista.setString(4, collezionista.getPassword());
            
            if ("".equals(collezionista.getCellulare())) {
                storeCollezionista.setNull(5, java.sql.Types.VARCHAR);
            } else {
                storeCollezionista.setString(5, collezionista.getCellulare());
            }
            
            if (storeCollezionista.executeUpdate() == 1) {
                //per leggere la chiave generata dal database per il record appena inserito
                try (ResultSet keys = storeCollezionista.getGeneratedKeys()) {
                       
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            //aggiornaimo la chiave anche nell oggetto di Collezione
                            collezionista.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            dataLayer.getCache().add(Collezionista.class, collezionista);
                        }
                    }
                }
            
            // questo "if" deve essere eseguto sia quando si fa la create che l'update della Collezione 
            if (collezionista instanceof DataItemProxy) {
                ((DataItemProxy) collezionista).setModified(false);
            }
           
        } catch (SQLException ex) {
        try {
            throw new DataException("Unable to store Collezionista", ex);
        } catch (DataException ex1) {
            Logger.getLogger(CollezionistaDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex1);
        }
        } catch (DataException ex) {
            Logger.getLogger(CollezionistaDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }

    @Override
    public Collezionista getCollezionistaByNickname(String nickname) throws DataException {
       Collezionista collezionista = null;
        
        try {
            getCollezionistaByNickname.setString(1, nickname);
            try (ResultSet rs = getCollezionistaByNickname.executeQuery()) {
                // il nickname di un Collezionista è univoco
                if (rs.next()) {
                    try {
                        collezionista = getCollezionistaById(rs.getInt("ID"));
                    } catch (DataException ex) {
                        Logger.getLogger(CollezionistaDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezionista by nickname", ex);
        }
        return collezionista;
    }

    @Override
    public List<Genere> getGeneriPreferiti(Collezionista collezionista) throws DataException{
        List<Genere> result = new ArrayList<Genere>();
        
        try{
            getGenerePreferito.setInt(1, collezionista.getKey()); 
            
            try(ResultSet rs = getGenerePreferito.executeQuery()){
                int count = 0;
                
                while (rs.next()){
                    Genere g = Genere.values()[rs.getInt("d.IDgenere") - 1];
                    result.add(g);
                    
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
    
    @Override
    public List<Collezionista> getCondivisioniByCollezione(Collezione collezione) throws DataException {
        List<Collezionista> result = new ArrayList<Collezionista>();
        
        try {
            getCondivisioniByCollezione.setInt(1, collezione.getKey());
            
            try (ResultSet rs = getCondivisioniByCollezione.executeQuery()) {
                while (rs.next()) {
                    result.add(getCollezionistaById(rs.getInt("IDcollezionista")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load condivisioni by Collezione", ex);
        }
        return result;
    }

    @Override
    public Integer login(String nickname, String password) throws DataException {
        Integer id = null;
        
        try {
            login.setString(1, nickname);
            login.setString(2, password);

            try (ResultSet rs = login.executeQuery()) {
                if (rs.next()) {
                    // caso in cui l'autenticazione ha avuto successo
                    id = rs.getInt("ID");
                    return id;
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to process the login", ex);
        }
        
        return id; // caso in cui l'autenticazione non ha avuto successo   
    }

    @Override
    public Collezionista getCollezionistaByDisco(Disco disco) throws DataException {
        Collezionista collezionista = null;
        
        try {
            getCollezionistaByDisco.setInt(1, disco.getKey());
            
            try (ResultSet rs = getCollezionistaByDisco.executeQuery()) {
                if (rs.next()) {
                    collezionista = getCollezionistaById(rs.getInt("c.IDcollezionista")); 
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Collezionista by Disco", ex);
        }
        
        return collezionista;
    }
}

