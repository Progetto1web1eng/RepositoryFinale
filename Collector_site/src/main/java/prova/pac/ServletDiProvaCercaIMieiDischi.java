/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.CopieStato;
import collector_site.data.impl.Genere;
import collector_site.data.model.Artista;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.framework.data.DataException;
import collector_site.framework.result.ProvaConfig;
import collector_site.framework.utils.Service;
import freemarker.core.ParseException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author fabri
 */
public class ServletDiProvaCercaIMieiDischi extends ServletDiProvaCollector_siteBaseController {
   
    private void cerca_InputAction(HttpServletRequest request, HttpServletResponse response,Map<String,Object> dataM, int IDcollezionista) throws DataException{
        try {
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            //completo la sideBar con la lista di collezioni
            Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
            List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
            dataM.put("collezioni",collezioni);
            
            String inputDaCercare = request.getParameter("cercaIMieiDischi");
            
            if(  !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":T"))&&
                    !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":A"))&&
                    !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":G"))
                    ){
                // assegno un tipo di ricerca di default nel caso in cui l'utente non abbia scelto i suggerimenti 
                inputDaCercare = inputDaCercare+":T";
               
                
            }
            String inputSenzaPlaceH = inputDaCercare.substring(0,inputDaCercare.length()-2);
                
            if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":T")){ //ricerca per titolo
                dataM.put("numero",4);
                List<Disco> listD = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDischiByNome(inputSenzaPlaceH, collezionista);
                if(listD.isEmpty()){
                        // caso in cui non abbiamo trovato nulla
                    dataM.put("hidden", 2);
                }else{
                    // caso in cui abbiamo un riscontro
                    listD = Service.deleteSame(listD);
                    

                    // trovo tutti gli artisti di ogni disco 
                    List<Artista> artistiList = new ArrayList();
                    for(Disco d : listD){
                        artistiList.add(((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaByDisco(d));
                    }
                     

                    // trovo le copie per ogni disco per poi aggiungere una lista al data model
                    List<List<CopieStato>> csList = new ArrayList();
                    List<CopieStato> tempList = new ArrayList();
                    for (Disco d : listD){
                        tempList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, collezionista);
                        csList.add(tempList);
                    }
               
                    
                    
                    dataM.put("csList",csList);
                    dataM.put("artistiList", artistiList);
                    dataM.put("hidden", 1);
                    dataM.put("dischiList",listD);
                }
                t.process(dataM, response.getWriter());
            }
                
            else if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":A")){ //ricerca per artista
                dataM.put("numero",4);
                Artista artista = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaNomeDarte(inputSenzaPlaceH);
                if(artista==null){
                     dataM.put("hidden", 2);
                      t.process(dataM, response.getWriter());
                }else{
                List<Disco> listD = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDischiByArtista(artista, collezionista);
                
                if(listD.isEmpty()){
                        // caso in cui non abbiamo trovato nulla
                    dataM.put("hidden", 2);
                }else{
                     // caso in cui abbiamo un riscontro
                    listD = Service.deleteSame(listD);
                    List<Artista> artistiList = new ArrayList();
                    for ( Disco d : listD){
                        artistiList.add(artista);
                    }
                    
                     // trovo le copie per ogni disco per poi aggiungere una lista al data model
                    List<List<CopieStato>> csList = new ArrayList();
                    List<CopieStato> tempList = new ArrayList();
                    for (Disco d : listD){
                        tempList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, collezionista);
                        csList.add(tempList);
                    }
               
                    
                    
                    dataM.put("csList",csList);
                    dataM.put("artistiList", artistiList);  
                    dataM.put("hidden", 1);
                    dataM.put("dischiList",listD);
                }
                t.process(dataM, response.getWriter());
                }
            }
            else if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":G")){ //ricerca per genere
                inputSenzaPlaceH = inputSenzaPlaceH.toLowerCase();
                String firstLetStr = inputSenzaPlaceH.substring(0, 1);
                String remLetStr = inputSenzaPlaceH.substring(1);
                firstLetStr = firstLetStr.toUpperCase();
                inputSenzaPlaceH = firstLetStr + remLetStr;
                dataM.put("numero",4);
                if(inputSenzaPlaceH.equals("Rock")||
                    inputSenzaPlaceH.equals("Metal")||
                        inputSenzaPlaceH.equals("Pop")||
                        inputSenzaPlaceH.equals("Blues")||
                        inputSenzaPlaceH.equals("Classico")||
                        inputSenzaPlaceH.equals("Country")||
                        inputSenzaPlaceH.equals("Dance")||
                        inputSenzaPlaceH.equals("Folk")||
                        inputSenzaPlaceH.equals("House")||
                        inputSenzaPlaceH.equals("Indie")||
                        inputSenzaPlaceH.equals("Jazz")||
                        inputSenzaPlaceH.equals("Latino")
                        ){
                    List<Disco> listD = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDischiByGenere(Genere.valueOf(inputSenzaPlaceH), collezionista);

                if(listD.isEmpty()){
                        // caso in cui non abbiamo trovato nulla
                    dataM.put("hidden", 2);
                }else{
                        // caso in cui abbiamo un riscontro
                        listD = Service.deleteSame(listD);
                    // trovo tutti gli artisti di ogni disco 
                    List<Artista> artistiList = new ArrayList();
                    for(Disco d : listD){
                        artistiList.add(((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaByDisco(d));
                    }
                    
                     // trovo le copie per ogni disco per poi aggiungere una lista al data model
                    List<List<CopieStato>> csList = new ArrayList();
                    List<CopieStato> tempList = new ArrayList();
                    for (Disco d : listD){
                        tempList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, collezionista);
                        csList.add(tempList);
                    }
               
                    
                    
                    dataM.put("csList",csList);
                    dataM.put("artistiList", artistiList);  
                    dataM.put("hidden", 1);
                    dataM.put("dischiList",listD);
                }
                t.process(dataM, response.getWriter());
                }else{
                        // caso in cui non abbiamo trovato nulla
                    dataM.put("hidden", 2);
                     t.process(dataM, response.getWriter());
                
                }
               
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        try {
            HttpSession s = request.getSession(false);
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
            if(request.getParameter("cercaIMieiDischi")==null){
                // estraggo le collezioni dal collezionista che ha effettuato il login per la visualizzazione del side menu
                Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById((int)s.getAttribute("id"));
                List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                dataM.put("collezioni",collezioni);
                dataM.put("numero",4);
                dataM.put("hidden",0); //non verrà visualizzato il div con la lista di dischi
                t.process(dataM, response.getWriter());
            }else{
                cerca_InputAction(request,response,dataM,(int)s.getAttribute("id"));
            }
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | TemplateException | DataException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

  