/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.DAO;

/**
 *
 * @author stefa
 */
import collector_site.data.impl.CopieStato;
import collector_site.data.impl.StatoDisco;
import collector_site.data.model.Disco;

// import riguardanti il framework
import collector_site.framework.data.DAO;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataLayer;

// import SQL
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

// import Java
import java.util.List;

/**
 *
 * @author stefa
 */
public class CopieStatoDAO_MySQL extends DAO implements CopieStatoDAO {

    private PreparedStatement createCopieStato;
    private PreparedStatement updateCopieStato;
    private PreparedStatement getCopieStatoByDisco;

    public CopieStatoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            createCopieStato = connection.prepareStatement("INSERT INTO colleziona (numCopieDisco,IDstatoDisco,statoDisco,IDcollezionista,IDdisco) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            updateCopieStato = connection.prepareStatement("UPDATE colleziona c SET numCopieDisco= ? WHERE c.IDcollezionista = ? and c.IDdisco = ? and c.IDstatoDisco = ?");
            getCopieStatoByDisco = connection.prepareStatement("SELECT * FROM colleziona WHERE IDdisco=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing CopieStato data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            createCopieStato.close();
            updateCopieStato.close();
            getCopieStatoByDisco.close();
        } catch (SQLException ex) {
        }
        super.destroy();
    }

    @Override
    public CopieStato createCopieStato() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void updateCopieStato(StatoDisco stato, int numCopieDisco) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CopieStato> getCopieStatoByDisco(Disco disco) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
