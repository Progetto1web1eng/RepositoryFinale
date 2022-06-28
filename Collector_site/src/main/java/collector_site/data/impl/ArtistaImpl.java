/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.impl;

import collector_site.framework.data.DataItemImpl;
import collector_site.data.model.Artista;
import collector_site.data.model.Disco;
import collector_site.data.model.Traccia;
import java.util.List;

/**
 *
 * @author mauri
 */
public class ArtistaImpl extends DataItemImpl<Integer> implements Artista{
    
    private String nomeDarte;
    private List<Artista> componenti; 
    //1) se componenti == null allora l'Artista non appartiene a nessun gruppo
    //2) se componenti ha un solo elemento allora quell'elemnto è il gruppo di appartenenza
    //3) se componenti ha più di un solo elemento allora il nomeDarte sarà il nome del gruppo e nella lista ci sono i partecipanti
    private List<Disco> dischiIncisi;
    private List<Traccia> tracceIncise;
    private Ruolo ruolo;

    
    public ArtistaImpl() {
        super();
        nomeDarte = "";
        componenti = null;
        dischiIncisi = null;
        tracceIncise = null;
        ruolo = null;
    }

   

   
    
    @Override
    public String getNomeDarte(){
        return nomeDarte;
    }
    
    @Override
    public void setNomeDarte(String nomeDarte){
        this.nomeDarte = nomeDarte;
    }
    @Override
    public List<Artista> getComponenti(){
        return componenti;
    }
    @Override
    public void setComponenti(List<Artista> componenti){
        this.componenti = componenti;
    }
    
    @Override
    public List<Disco> getDischiIncisi(){
        return dischiIncisi;
    }
    
    @Override
    public void setDischiIncisi(List<Disco> dischiIncisi){
        this.dischiIncisi = dischiIncisi;
    }
    
    @Override
    public List<Traccia> getTracceIncise(){
        return tracceIncise;
    }
    
    @Override
    public void setTracceIncise(List<Traccia>tracceIncise){
        this.tracceIncise = tracceIncise;
    }
    @Override
    public Ruolo getRuolo(){
        return this.ruolo;
    }
    @Override
    public void setRuolo(Ruolo ruolo){
        this.ruolo=ruolo;
    }
    
}
