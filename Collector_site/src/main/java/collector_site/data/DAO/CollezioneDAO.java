/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.DAO;

import collector_site.framework.data.DataException;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.proxy.CollezioneProxy;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author fabri
 */
public interface CollezioneDAO {
    
    Collezione createCollezione();
    CollezioneProxy createCollezione(ResultSet rs)throws DataException;
    void deleteCollezione(Collezione collezione);
    
    //controllare bene tutti insieme
    void updateCondivisione(Collezionista collezionista)throws DataException;
    
    void storeCollezione(Collezione collezione) throws DataException;
    
   
    
    
    
    Collezione getCollezioneById(int id) throws DataException;
    //possiamo prendere una collezione dal collezionista che l'ha creata
    List<Collezione> getCollezioneByCollezionista(Collezionista collezionista) throws DataException;
    List<Collezione> getCollezioneByDisco(Disco disco) throws DataException;
    List<Collezione> getCollezioneByBarcodeDisco(String barcode) throws DataException;
    List<Collezione> getCollezioneByNomeDisco(String nomeDisco) throws DataException;
    
    public void deleteCondivisione(Collezione collezione, Collezionista collezionista) throws DataException;
    public void addCondivisione(Collezione collezione, Collezionista collezionista) throws DataException;

    List<Collezione> getCollezioniCondiviseToCollezionista(Collezionista collezionista) throws DataException;
    List<Collezione> getCollezioniPrivateCondiviseToCollezionista(Collezionista collezionista) throws DataException;
    List<Collezione> getCollezioniAccessibili(Collezionista collezionista_target, Collezionista collezionista_loggato) throws DataException;
    List<Collezione> getCollezioniAccessibiliByNome(String nomeCollezione, Collezionista collezionista) throws DataException;
}
