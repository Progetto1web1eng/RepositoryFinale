/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.model;

import collector_site.data.impl.CopieStato;
import collector_site.data.impl.Genere;
import collector_site.data.impl.StatoDisco;
import collector_site.data.impl.Tipo;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataItem;
import java.awt.Image;
import java.util.Date;
import java.util.List;

/**
 *
 * @author fabri
 */
public interface Disco extends DataItem<Integer>{
            
    String getNomeDisco();
    void setNomeDisco(String nomeDisco);
    
    String getEtichetta();
    void setEtichetta(String etichetta);
    
    String getBarcode();
    void setBarcode(String barcode);
    
    int getAnno()throws DataException;
    void setAnno(int anno)throws DataException;
    
    Genere getGenere();
    void setGenere(Genere genere);
    
    public Tipo getTipo();
    public void setTipo(Tipo tipo);
    
    List<Immagine> getImmagini();
    void setImmagini(List<Immagine> immagini);
    
    List<Traccia> getTracce();
    void setTracce(List<Traccia> tracce);
    
    List<Artista> getCompositori()throws DataException;
    void setCompositori(List<Artista> compositori);
    
    Collezionista getCollezionista();
    void setCollezionista(Collezionista collezionista);
    public List<CopieStato> getCopieStati();
    public void setCopieStati(List<CopieStato> copieStati);
    
}
