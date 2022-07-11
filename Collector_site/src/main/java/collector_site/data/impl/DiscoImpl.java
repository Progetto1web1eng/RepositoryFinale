  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.impl;

import collector_site.framework.data.DataItemImpl;
import collector_site.data.model.Artista;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import collector_site.data.model.Traccia;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mauri
 */
public class DiscoImpl extends DataItemImpl<Integer> implements Disco{
	
	private String nomeDisco;
	private String barcode;
	private int anno;
	private String etichetta;
	private Genere genere;
        private Tipo tipo;
	private Collezionista collezionista;
	private List<Artista> compositori;
	private List<Immagine> immagini;
	private List<Traccia> tracce;
	private List<CopieStato> copieStati;

	public DiscoImpl() {
            super();
            nomeDisco = "";
            barcode = "";
            anno=0;
            etichetta = "";
            genere = null;
            collezionista = null;
            compositori = null;
            immagini = null;
            tracce = null;
            tipo = null;
            
            copieStati = new ArrayList();
            
            CopieStato nuovo = new CopieStato();
            nuovo.setStato(StatoDisco.NUOVO);
            
            CopieStato usato = new CopieStato();
            usato.setStato(StatoDisco.USATO);
            
            copieStati.add(nuovo);
            copieStati.add(usato);
        }
        
        @Override
	public String getNomeDisco() {
		return nomeDisco;
	}
	@Override
	public void setNomeDisco(String nomeDisco) {
		this.nomeDisco = nomeDisco;
	}
	@Override
	public String getBarcode() {
		return barcode;
	}
	@Override
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	@Override
	public int getAnno() {
		return anno;
	}
	@Override
	public void setAnno(int anno) {
		this.anno = anno;
	}
	@Override
	public String getEtichetta() {
		return etichetta;
	}
	@Override
	public void setEtichetta(String etichetta) {
		this.etichetta = etichetta;
	}
	@Override
	public Genere getGenere() {
		return genere;
	}
	@Override
	public void setGenere(Genere genere) {
		this.genere = genere;
	}
        @Override
	public Collezionista getCollezionista() {
		return collezionista;
	}
        @Override
	public void setCollezionista(Collezionista collezionista) {
		this.collezionista = collezionista;
	}
        @Override
	public List<Artista> getCompositori() {
		return compositori;
	}
        @Override
	public void setCompositori(List<Artista> compositori) {
		this.compositori = compositori;
	}
        @Override
	public List<Immagine> getImmagini() {
		return immagini;
	}
        @Override
	public void setImmagini(List<Immagine> immagini) {
		this.immagini = immagini;
	}
        @Override
	public List<Traccia> getTracce() {
		return tracce;
	}
        @Override
	public void setTracce(List<Traccia> tracce) {
		this.tracce = tracce;
	}
        
        @Override
        public Tipo getTipo() {
            return tipo;
        }

        @Override
        public void setTipo(Tipo tipo) {
            this.tipo = tipo;
        }
        
        @Override
        public void setCopieStati(List<CopieStato> copieStati) {
            this.copieStati = copieStati;
        }

        @Override
        public List<CopieStato> getCopieStati() {
            return copieStati;
        }
        
	
    
}
