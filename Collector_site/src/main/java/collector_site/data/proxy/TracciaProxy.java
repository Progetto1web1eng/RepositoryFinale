/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.proxy;

import collector_site.data.DAO.ArtistaDao;
import collector_site.data.DAO.DiscoDao;
import collector_site.data.impl.TracciaImpl;
import collector_site.data.model.Artista;
import collector_site.data.model.Disco;
import collector_site.data.model.Traccia;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataItemProxy;
import collector_site.framework.data.DataLayer;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mauri
 */
public class TracciaProxy extends TracciaImpl implements DataItemProxy{
   protected boolean modified;
   protected int disco_key = 0;
   protected DataLayer dataLayer;
   
   public TracciaProxy(DataLayer d){
       super();
       this.dataLayer = d;
       this.modified = false;
       this.disco_key = 0;    
   }
   
   @Override
   public Disco getDisco(){
       if(super.getDisco()==null && disco_key>0){
           try{
               super.setDisco(((DiscoDao)dataLayer.getDAO(Disco.class)).getDisco(disco_key));
           } catch (DataException ex) {
               Logger.getLogger(TracciaProxy.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
       return super.getDisco();
   }
   
   @Override
   public void setTitolo(String titolo){
       super.setTitolo(titolo);
       this.modified = true;       
   }
   @Override
   public void setDurata(Time durata){
       super.setDurata(durata);       
       this.modified = true;
   }
   @Override 
   public void setDisco(Disco disco){
       super.setDisco(disco);
       this.modified = true;
       this.disco_key = disco.getKey();
   }
   
   @Override 
   public void setArtisti(List<Artista> artisti){
       super.setArtisti(artisti);
       this.modified = true;
   }
   
    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    @Override
    public boolean isModified() {
        return modified;
    }
    public void setDiscoKey(int disco_key)throws DataException{
        this.disco_key = disco_key;
        //resettiamo cache disco
        super.setDisco(null);
    }
    
}
