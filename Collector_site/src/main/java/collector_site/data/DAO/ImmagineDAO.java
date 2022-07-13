/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.DAO;

import collector_site.framework.data.DataException;
import collector_site.data.model.Collezione;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import java.util.List;

/**
 *
 * @author mauri
 */
public interface ImmagineDAO {
    Immagine createImmagine();
    void deleteImmagine(Immagine immagine);
    Immagine getImmagineById(int id)throws DataException;
    List<Immagine> getImmaginiByDisco(Disco disco)throws DataException;
    List<Immagine> getImmagini() throws DataException;
    void storeImmagine(Immagine immagine) throws DataException;
}
