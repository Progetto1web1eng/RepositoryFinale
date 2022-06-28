/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.model;

import collector_site.data.impl.CopieStato;
import collector_site.framework.data.DataItem;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author fabri
 */
public interface Collezionista extends DataItem<Integer>{

    String getUsername();
    void setUsername(String username);
    
    String getNickname();
    void setNickname(String nickname);
    
    String getEmail();
    void setEmail(String email);
    
    String getPassword();
    void setPassword(String password);
    
    String getCellulare();
    void setCellulare(String cellulare);
    
    HashMap<Disco,CopieStato> getDischiEInfo();
    void setDischiEInfo(HashMap<Disco,CopieStato> dischiEInfo);
    
    List<Collezione> getCollezioni();
    void setCollezioni(List<Collezione> collezioni);
    
    List<Collezione> getCollezioniCondivise();
    void setCollezioniCondivise(List<Collezione> collezioniCondivise);
}
    
    
    