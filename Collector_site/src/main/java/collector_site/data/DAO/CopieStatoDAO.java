/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.DAO;

import collector_site.data.impl.CopieStato;
import collector_site.data.impl.StatoDisco;
import collector_site.data.model.Disco;
import java.util.List;

/**
 *
 * @author mauri
 */
public interface CopieStatoDAO {
    CopieStato createCopieStato();
    void updateCopieStato(StatoDisco stato,int numCopieDisco);
   // List<CopieStato> getCopieStatoByStato(StatoDisco stato );
    List<CopieStato> getCopieStatoByDisco(Disco disco);
    
    
    
    
    
}
