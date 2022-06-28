/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.model;

import collector_site.data.impl.Ruolo;
import collector_site.framework.data.DataItem;
import java.util.List;

/**
 *
 * @author mauri
 */
public interface Artista extends DataItem<Integer> {
    
    String getNomeDarte();
    void setNomeDarte(String nomeDarte);
    
    List<Artista> getComponenti();
    void setComponenti (List<Artista> componenti);
    
    List<Disco> getDischiIncisi();
    void setDischiIncisi(List<Disco> dischiIncisi);
    
    List<Traccia> getTracceIncise();
    void setTracceIncise(List<Traccia> tracceIncise);
    
    Ruolo getRuolo();
    void setRuolo(Ruolo ruolo);
    
    
}
