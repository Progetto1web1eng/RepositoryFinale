/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.proxy;

import collector_site.data.DAO.CollezionistaDAO;
import collector_site.data.DAO.DiscoDao;
import collector_site.data.impl.CollezioneImpl;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataItemProxy;
import collector_site.framework.data.DataLayer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mauri
 */
public class CollezioneProxy extends CollezioneImpl implements DataItemProxy {
    
    protected boolean modified;
    protected int collezionista_key = 0;
    protected DataLayer dataLayer;
    
    public CollezioneProxy(DataLayer d){
        super();
        this.dataLayer = d;
        this.modified = false;
        this.collezionista_key = 0;
    }
    
    @Override
    public void setKey(Integer key){
        super.setKey(key);
        this.modified = true;
    }
    
    @Override
    public Collezionista getCreatore() {
        
        if (super.getCreatore() == null && collezionista_key > 0) {
            try {
                super.setCreatore(((CollezionistaDAO) dataLayer.getDAO(Collezionista.class)).getCreatore(collezionista_key));
             } catch (DataException ex) {
                Logger.getLogger(CollezioneProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCreatore();
    }
    @Override
    public List<Collezionista> getCondivisioni(){
        if(super.getCondivisioni()==null){
            try{
                super.setCondivisioni((List<Collezionista>) ((CollezionistaDAO)dataLayer.getDAO(Collezionista.class)).getCollezionistaByCollezione(this));
            }catch (DataException ex) {
                Logger.getLogger(CollezionistaProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
         return super.getCondivisioni();
    }
    @Override
    public List<Disco> getDischi(){
        if(super.getDischi()==null){
            try{
                super.setDischi(((DiscoDao)dataLayer.getDAO(Disco.class)).getDiscoByCollezione(this));
            }catch (DataException ex) {
                Logger.getLogger(CollezionistaProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        return super.getDischi();
    }
    @Override
    public void setCreatore(Collezionista collezionista) throws DataException{
        super.setCreatore(collezionista);
        this.collezionista_key = collezionista.getKey();
        this.modified = true;
    }
    @Override
    public void setNomeCollezione(String nomeCollezione){
        super.setNomeCollezione(nomeCollezione);
        this.modified = true;
    }
    @Override
    public void setPubblico(boolean pubblico){
        super.setPubblico(pubblico);
        this.modified = true;
    }
    @Override
    public void setDischi(List<Disco> dischi){
        super.setDischi(dischi);
        this.modified = true;
        
    }
    @Override
    public void setCondivisioni(List<Collezionista>condivisioni){
        super.setCondivisioni(condivisioni);
        this.modified = true;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
                }
    
    //override qua da mettere se qualcosa non funziona
    public void setCollezionistaKey(int collezionista_key) throws DataException{
        this.collezionista_key = collezionista_key;
        //resettiamo cache collezionista
        super.setCreatore(null);
    }
    
}
