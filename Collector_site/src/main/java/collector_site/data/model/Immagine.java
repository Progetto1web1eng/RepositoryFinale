/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.model;
import collector_site.framework.data.DataItem;
import java.util.List;
/**
 *
 * @author mauri
 */
public interface Immagine extends DataItem<Integer> {
    
    String getNomeImmagine();
    void setNomeImmagine(String nomeImmagine);
    
    String getFilename();
    void setFilename(String filename);
    
    String getImgType();
    void setImgType(String imgType);
    
    long getDimensioneImmagine();
    void setDimensioneImmagine(long dimensioneImmagine);
    
    Disco getDiscoImg();
    void setDiscoImg(Disco discoImg);
    
}
