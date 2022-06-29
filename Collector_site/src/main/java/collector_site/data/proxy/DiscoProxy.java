/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.proxy;

import collector_site.data.DAO.ArtistaDao;
import collector_site.data.DAO.CollezionistaDAO;
import collector_site.data.DAO.ImmagineDAO;
import collector_site.data.DAO.TracciaDAO;
import collector_site.data.impl.DiscoImpl;
import collector_site.data.impl.Genere;
import collector_site.data.impl.Tipo;
import collector_site.data.model.Artista;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Immagine;
import collector_site.data.model.Traccia;
import collector_site.framework.data.DataException;
import collector_site.framework.data.DataItemProxy;
import collector_site.framework.data.DataLayer;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mauri
 */
public class DiscoProxy extends DiscoImpl implements  DataItemProxy{
 
    protected boolean modified;
    protected int artista_key = 0;
    protected int collezionista_key = 0;
    protected DataLayer dataLayer;
    
    public DiscoProxy(DataLayer d){
        super();
        this.modified = false;
        this.artista_key = 0;
        this.collezionista_key = 0;
        
        
    }
    @Override 
    public void setNomeDisco (String nomeDisco){
        super.setNomeDisco(nomeDisco);
        this.modified = true;
                
    }
    @Override
    public void setBarcode (String barcode){
        super.setBarcode(barcode);
        this.modified = true;
    }
    @Override
    public void setAnno(int anno){
        super.setAnno(anno);
        this.modified = true;
    }
    @Override 
        public void setEtichetta(String etichetta){
        super.setEtichetta(etichetta);
        this.modified = true;
    }
    @Override
        public void setGenere(Genere genere){
            super.setGenere(genere);
            this.modified = true;
        }    
    @Override
    public void setTipo(Tipo tipo){
        super.setTipo(tipo);
        this.modified = true;
        
    }
    @Override
    public void setCollezionista(Collezionista collezionista){
        super.setCollezionista(collezionista);
        this.modified = true;
    }
    @Override
    public void setCompositori(List<Artista> compositori){
        super.setCompositori(compositori);
        this.modified = true;
    }
    @Override
    public void setImmagini(List<Immagine> immagini){
        super.setImmagini(immagini);
        this.modified = true;
    }
    @Override
    public void setTracce(List<Traccia> tracce){
        super.setTracce(tracce);
        this.modified = true;
    }
        @Override
    public Collezionista getCollezionista() {
        if (super.getCollezionista() == null && collezionista_key > 0) {
            try {
                super.setCollezionista(((CollezionistaDAO) dataLayer.getDAO(Collezionista.class)).getCollezionistaById(collezionista_key));
            } catch (DataException ex) {
                Logger.getLogger(DiscoProxy.class.getName()).log(Level.SEVERE, null, ex);            }
        }
        
        return super.getCollezionista();
    }
    @Override
    public List<Artista> getCompositori() {
        
        if (super.getCompositori() == null) {
            try {
                super.setCompositori(((ArtistaDao) dataLayer.getDAO(Artista.class)).getArtisti(artista_key));
            } catch (DataException ex) {
                Logger.getLogger(DiscoProxy.class.getName()).log(Level.SEVERE, null, ex);            }
        }  
        return super.getCompositori();
    }
    @Override
    public List<Immagine> getImmagini(){
        if(super.getImmagini()==null){
        try{
            super.setImmagini(((ImmagineDAO)dataLayer.getDAO(Immagine.class)).getImmaginiByDisco(this));
        } catch (DataException ex) {
                Logger.getLogger(DiscoProxy.class.getName()).log(Level.SEVERE, null, ex);            }
        }  
        return super.getImmagini();
    }
    @Override
    public List<Traccia> getTracce(){
        if(super.getTracce()== null){
            try{
                super.setTracce(((TracciaDAO)dataLayer.getDAO(Traccia.class)).getTracceByDisco(this));
            } catch (DataException ex) {
                Logger.getLogger(DiscoProxy.class.getName()).log(Level.SEVERE, null, ex);            }
        }
        return super.getTracce();
        }
    

    
    @Override
    public boolean isModified() {

        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified=modified;

    }
    }
