/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.impl;


import collector_site.framework.data.DataItemImpl;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import java.sql.Timestamp;

/**
 *
 * @author mauri
 */
public class ImmagineImpl extends DataItemImpl<Integer> implements Immagine{
	
	private String nomeImmagine;
	private String filename;
	private String imgtype;
	private long dimensioneImmagine;
	private Disco discoImg;
        private String digest;
        private Timestamp updated;
        
        public ImmagineImpl() {
            super();
            nomeImmagine = "";
            filename = "";
            imgtype = "";
            dimensioneImmagine = 0;
            discoImg = null;
            digest = "";
            updated = null;
        }

   

   
    @Override
    public String getDigest() {
        return digest;
    }

    @Override
    public void setDigest(String digest) {
        this.digest = digest;
    }

    @Override
    public Timestamp getUpdated() {
        return updated;
    }

    @Override
    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
        
	@Override
	public String getNomeImmagine() {
		return nomeImmagine;
	}
	@Override
	public void setNomeImmagine(String nomeImmagine) {
		this.nomeImmagine = nomeImmagine;
	}
	@Override
	public String getFilename() {
		return filename;
	}
	@Override
	public void setFilename(String filename) {
		this.filename = filename;
	}
	@Override
	public String getImgType() {
		return imgtype;
	}
	@Override
	public void setImgType(String imgtype) {
		this.imgtype = imgtype;
	}
	@Override
	public long getDimensioneImmagine() {
		return dimensioneImmagine;
	}
	@Override
	public void setDimensioneImmagine(long dimensioneImmagine) {
		this.dimensioneImmagine = dimensioneImmagine;
	}
	@Override
	public Disco getDiscoImg() {
		return discoImg;
	}
	@Override
	public void setDiscoImg(Disco discoImg) {
		this.discoImg = discoImg;
	}
	
    
    
}
