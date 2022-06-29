/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.proxy;

import collector_site.data.impl.ImmagineImpl;
import collector_site.framework.data.DataItemProxy;
import collector_site.framework.data.DataLayer;

/**
 *
 * @author mauri
 */
public class ImmagineProxy extends ImmagineImpl implements DataItemProxy{
   
    protected boolean modified;
    protected DataLayer dataLayer;
    
    public ImmagineProxy(DataLayer d){
        super();
        this.dataLayer = d;
        this.modified = false;
    }
    @Override
    public void setNomeImmagine(String nomeImmagine){
        super.setNomeImmagine(nomeImmagine);
        this.modified = true;
    }
    @Override
    public void setFilename(String fileName){
        super.setFilename(fileName);
        this.modified = true;
    }
    @Override
    public void setImgType(String imgtype){
        super.setImgType(imgtype);
        this.modified = true;
    }
    @Override
    public void setDimensioneImmagine(long dimensioneImmagine){
        super.setDimensioneImmagine(dimensioneImmagine);
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
}
