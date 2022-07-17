/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.model;

import collector_site.framework.data.DataItem;
import java.sql.Time;
import java.util.List;



/**
 *
 * @author fabri
 */
public interface Traccia extends DataItem<Integer> {
    
    Time getDurata();
    void setDurata(Time durata);
    
    String getTitolo();
    void setTitolo(String titolo);
    
    Disco getDisco();
    void setDisco(Disco disco);
    
    List<Artista> getArtisti();
    void setArtisti(List<Artista> artisti);
    
    
}
