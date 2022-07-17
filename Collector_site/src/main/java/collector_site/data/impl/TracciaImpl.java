/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.impl;

import collector_site.framework.data.DataItemImpl;
import collector_site.data.model.Artista;
import collector_site.data.model.Disco;


import collector_site.data.model.Traccia;
import java.sql.Time;
import java.util.List;

/**
 *
 * @author mauri
 */
public class TracciaImpl extends DataItemImpl<Integer> implements Traccia{
    
	private String titolo;
	private Time durata;
	private Disco disco;
	private List<Artista> artisti;
       
        public TracciaImpl() {
            super();
            titolo = "";
            durata = null;
            disco = null;
            artisti = null;
        }
        
        @Override
	public String getTitolo() {
		return titolo;
	}
        @Override
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
        @Override
	public Time getDurata() {
		return durata;
	}
        @Override
	public void setDurata(Time durata) {
		this.durata = durata;
	}
        @Override
	public Disco getDisco() {
		return disco;
	}
        @Override
	public void setDisco(Disco disco) {
		this.disco = disco;
	}
        @Override
	public List<Artista> getArtisti() {
		return artisti;
	}
        @Override
	public void setArtisti(List<Artista> artisti) {
		this.artisti = artisti;
	}
	
    
    
}
