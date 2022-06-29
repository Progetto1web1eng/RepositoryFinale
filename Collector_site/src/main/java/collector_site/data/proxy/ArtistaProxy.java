/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.proxy;

import collector_site.data.impl.ArtistaImpl;
import collector_site.data.model.Artista;
import collector_site.data.model.Disco;
import collector_site.data.model.Traccia;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataItemProxy;
import collector_site.framework.data.DataLayer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import collector_site.data.DAO.DiscoDao;
import collector_site.data.impl.Ruolo;


/**
 *
 * @author mauri
 */
public class ArtistaProxy extends ArtistaImpl implements DataItemProxy{
    
    protected boolean modified;
    protected int traccia_key = 0;
    protected int disco_key = 0;
    protected DataLayer dataLayer;
    
    public ArtistaProxy(DataLayer d){
        super();
        this.dataLayer = d;
        this.modified = false;
        this.traccia_key=0;
        this.disco_key=0;
        
    }
    
    @Override
    public void setKey(Integer key){
        super.setKey(key);
        this.modified = true;
    }
    
    @Override
    public void setNomeDarte(String nomeDarte){
        super.setNomeDarte(nomeDarte);
        this.modified = true;
    }
    
    @Override
    public void setComponenti(List<Artista> componenti){
        super.setComponenti(componenti);
        this.modified = true;
    }
    
    @Override
    public void setDischiIncisi(List<Disco>dischiIncisi){
        super.setDischiIncisi(dischiIncisi);
        this.modified = true;
    }
    
    @Override
    public void setTracceIncise(List<Traccia> tracceIncise){
        super.setTracceIncise(tracceIncise);
        this.modified = true;
    }
    
    @Override
    public void setRuolo(Ruolo ruolo){
        super.setRuolo(ruolo);
        this.modified = true;
    }
    
    @Override
    public List<Disco> getDischiIncisi(){
        if(super.getDischiIncisi()==null){
            try{
                super.setDischiIncisi(((DiscoDao) dataLayer.getDAO(Disco.class)).getDischiIncisi());   
            }catch(DataException ex){
                Logger.getLogger(ArtistaProxy.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        return super.getDischiIncisi();
    }
    
    //metodi base classe proxy
    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
                }
    
    
    
}
