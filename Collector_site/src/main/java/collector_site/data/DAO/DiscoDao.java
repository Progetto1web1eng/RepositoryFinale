/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.DAO;

import collector_site.data.impl.CopieStato;
import collector_site.data.impl.Genere;
import collector_site.framework.data.DataException;
import collector_site.data.model.Artista;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import collector_site.data.model.Traccia;
import collector_site.data.proxy.DiscoProxy;
import collector_site.data.impl.StatoDisco;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mauri
 */
public interface DiscoDao {
    Disco createDisco();
    DiscoProxy createDisco(ResultSet rs) throws DataException;
    void deleteDisco(Disco disco);
    Disco getDisco(int id) throws DataException;
    List<Disco> getDischi() throws DataException;
    //possiamo prendere un insieme di dischi dalla collezione in cui si trovano
    //il nome andrebbe modificato in getDischiByCollezione andando a modificare a cascata tutto il codice già scritto
    List<Disco> getDiscoByCollezione(Collezione collezione) throws DataException;
    List<Disco> getDischiByCollezionista(Collezionista collezionista) throws DataException;
    //possiamo prendere un disco da una traccia contenuta in esso
    Disco getDiscoByTraccia (Traccia traccia) throws DataException;
    //possiamo prendere un insieme di dischi selezionando l'artista
    List<Disco> getDischiByArtista(Artista artista)throws DataException;
    List<Disco> getDischiByArtista(Artista artista, Collezionista collezionista)throws DataException;
    //prendiamo una lista di dischi cercando il nome
    List<Disco> getDischiByNome(String nome) throws DataException;
    List<Disco> getDischiByNome(String nomeDisco, Collezionista collezionista) throws DataException;
    List<Disco> getDischiByGenere(Genere genere) throws DataException;
    List<Disco> getDischiByGenere(Genere genere, Collezionista collezionista) throws DataException;
    void storeDisco(Disco disco)throws DataException;
    public void updateQuantitaDisco(Disco disco, Collezionista collezionista, StatoDisco statoDisco, int quantitaDisco) throws DataException;
    void addDiscoToCollezione(Disco disco, Collezione collezione) throws DataException;
    void removeDiscoToCollezione(Disco disco, Collezione collezione) throws DataException;
    public List<Disco> getDischiIncisi()throws DataException;
    void setArtistaOfDisco(Disco disco, Artista artista) throws DataException;
    void addDiscoToCollezionista(Disco disco, Collezionista collezionista) throws DataException;
    public void getJson(String pathProgetto) throws DataException, IOException;
    List<CopieStato> getCopieStati(Disco disco, Collezionista collezionista) throws DataException;
}