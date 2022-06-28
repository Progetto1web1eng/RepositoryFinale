/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.model;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataItem;
import java.util.List;
/**
 *
 * @author mauri
 */
public interface Collezione extends DataItem<Integer> {
    
    boolean getPubblico();
    void setPubblico(boolean pubblico);
    
    String getNomeCollezione();
    void setNomeCollezione(String nomeCollezione);
    
    Collezionista getCreatore();
    void setCreatore(Collezionista creatore)throws DataException;
    
    List<Collezionista> getCondivisioni();
    void setCondivisioni ( List<Collezionista> condivisioni);
    
    List<Disco> getDischi();
    void setDischi (List<Disco> dischi);
    
}
