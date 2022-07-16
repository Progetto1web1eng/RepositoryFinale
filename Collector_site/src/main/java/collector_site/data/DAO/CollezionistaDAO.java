/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.data.DAO;

import collector_site.data.impl.Genere;
import collector_site.data.model.Artista;
import collector_site.framework.data.DataException;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import java.util.List;

/**
 *
 * @author fabri
 */
public interface CollezionistaDAO {
    
    Collezionista createCollezionista();
    void delteCollezionista(Collezionista collezionista);
    void updateCollezionista(String nickname,int cellulare);
    Collezionista getCollezionistaByUandP(String username,String password);
    Collezionista getCollezionistaById(int id) throws DataException;
    Collezionista getCollezionistaByCollezione(Collezione collezione) throws DataException;

    public Collezionista getCreatore(int collezionista_key);

    public List<Artista> getCompositori(int artista_key);
    
    void storeCollezionista(Collezionista collezionista);
    
    public List<Collezionista> getCollezionisti() throws collector_site.framework.data.DataException;
    
    public Collezionista getCollezionistaByNickname(String nickname) throws DataException;
    
    List<Genere> getGeneriPreferiti(Collezionista collezionista) throws DataException;
    public List<Collezionista> getCondivisioniByCollezione(Collezione collezione) throws DataException;
    Integer login(String nickname,String password) throws DataException;
    Collezionista getCollezionistaByDisco(Disco disco) throws DataException;
}
