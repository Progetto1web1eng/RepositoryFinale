/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.DAO;

import collector_site.framework.data.DataException;
import collector_site.data.model.Artista;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.proxy.ArtistaProxy;
import collector_site.data.proxy.DiscoProxy;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author mauri
 */
public interface ArtistaDao {
    Artista createArtista();
    ArtistaProxy createArtista(ResultSet rs) throws DataException;
    void deleteArtista(Artista artista);
    Artista getArtistaById(int id) throws DataException;
    List<Artista> getArtisti(int artista_key) throws DataException;
    List<Artista> getCompositori()throws DataException;
    public void storeArtista (Artista artista)throws DataException;
    public void storeComponenteGruppo (Artista artista, Integer idGruppoMusicale) throws DataException;
    Artista getArtistaByDisco (Disco disco)throws DataException;
    Artista getArtistiByGruppoMusicale (Artista gruppoMusicale) throws DataException;
    List<Artista> getArtistiPreferiti(Collezionista collezionista) throws DataException;
    public Artista getArtistaNomeDarte(String nomeDarte) throws DataException;
    public void getArtistiSingoliJson(String pathProgetto) throws DataException, IOException;
    public void getGruppiMusicaliJson(String pathProgetto) throws DataException, IOException;

}
