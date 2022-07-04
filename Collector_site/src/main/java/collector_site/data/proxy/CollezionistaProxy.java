/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.proxy;

import collector_site.data.DAO.CollezioneDAO;
import collector_site.data.impl.CollezionistaImpl;
import collector_site.data.model.Collezione;
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
public class CollezionistaProxy extends CollezionistaImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;
    
    public CollezionistaProxy(DataLayer d){
        super();
        this.dataLayer = d;
        this.modified = false;
    }
    @Override
    public void setKey(Integer key){
        super.setKey(key);
        this.modified = true;
    }
    @Override
    public List<Collezione> getCollezioni() {
    if (super.getCollezioni() == null) {
        try {
            super.setCollezioni(((CollezioneDAO) dataLayer.getDAO(Collezione.class)).getCollezioneByCollezionista(this));
            } catch (DataException ex) {
                Logger.getLogger(CollezionistaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCollezioni();
    }
    @Override
    public List<Collezione> getCollezioniCondivise(){
        if(super.getCollezioniCondivise()==null){
            try{
                super.setCollezioniCondivise(((CollezioneDAO)dataLayer.getDAO(Collezione.class)).getCollezioniCondiviseToCollezionista(this));
            }catch (DataException ex) {
                Logger.getLogger(CollezionistaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCollezioniCondivise();
    }
    @Override
    public void setCollezioni(List<Collezione> collezioni){
        super.setCollezioni(collezioni);
        this.modified = true;
    }
    @Override
    public void setUsername(String username){
        super.setUsername(username);
        this.modified = true;
    }
    @Override
    public void setNickname(String nickname){
        super.setNickname(nickname);
        this.modified = true;
    }
    @Override
    public void setEmail(String email){
        super.setEmail(email);
        this.modified = true;
    }
    @Override
    public void setPassword(String password){
        super.setPassword(password);
        this.modified = true;
    }
    @Override
    public void setCellulare(String cellulare){
        super.setCellulare(cellulare);
        this.modified = true;
    }
    @Override 
    public void setCollezioniCondivise(List<Collezione> collezioniCondivise){
        super.setCollezioniCondivise(collezioniCondivise);
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
    
     
    
}


