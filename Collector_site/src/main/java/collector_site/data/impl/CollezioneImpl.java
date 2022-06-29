/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.impl;

import collector_site.framework.data.DataItemImpl;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.framework.data.DataException;
import java.util.List;

/**
 *
 * @author mauri
 */
public class CollezioneImpl extends DataItemImpl<Integer> implements Collezione{
   
    private String nomeCollezione;
    private List<Collezionista> condivisioni; //lista di collezionista che hanno accesso a questa collezione se è privata
    private Collezionista creatore;
    private List<Disco> dischi;
    private boolean pubblico; // true se la collezione è pubblica false se è privata
    
    public CollezioneImpl() {
        super();
        nomeCollezione = "";
        condivisioni = null;
        creatore = null;
        dischi = null;
        pubblico = true;
    }
    
        @Override
	public String getNomeCollezione() {
		return nomeCollezione;
	}
        @Override
	public void setNomeCollezione(String nomeCollezione) {
		this.nomeCollezione = nomeCollezione;
	}
        @Override
	public List<Collezionista> getCondivisioni() {
		return condivisioni;
	}
        @Override
	public void setCondivisioni(List<Collezionista> condivisioni) {
		this.condivisioni = condivisioni;
	}
        @Override
	public Collezionista getCreatore() {
		return creatore;
	}
        @Override
	public void setCreatore(Collezionista creatore) throws DataException{
		this.creatore = creatore;
	}
        @Override
        public List<Disco> getDischi(){
            return this.dischi;
        }
        @Override
        public void setDischi(List<Disco> dischi){
            this.dischi=dischi;
        }
        @Override
        public boolean getPubblico(){
            return this.pubblico;
        }
        @Override
        public void setPubblico(boolean pubblico){
            this.pubblico=pubblico;
        }
    
}
