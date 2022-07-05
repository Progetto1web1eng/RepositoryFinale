/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.DAO;

import collector_site.framework.data.DataException;
import collector_site.data.model.Artista;
import collector_site.data.model.Disco;
import collector_site.data.model.Traccia;
import collector_site.data.proxy.TracciaProxy;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author mauri
 */
public interface TracciaDAO {
    Traccia createTraccia();
    TracciaProxy createTraccia(ResultSet rs)throws DataException;
    void deleteTraccia(Traccia traccia);
    public void storeTraccia(Traccia traccia) throws DataException;
    Traccia getTracciaById(int id)throws DataException;
    //questo è da rimuovere(?)
    List<Traccia> getTracciaByArtista(Artista artista) throws DataException;
    List<Traccia> getTracceByDisco(Disco disco) throws DataException;
    
}
