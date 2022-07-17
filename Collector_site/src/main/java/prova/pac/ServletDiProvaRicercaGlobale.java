/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.CopieStato;
import collector_site.data.model.Artista;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.model.Traccia;
import collector_site.framework.data.DataException;
import collector_site.framework.result.ProvaConfig;
import freemarker.core.ParseException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author fabri
 */
public class ServletDiProvaRicercaGlobale extends  ServletDiProvaCollector_siteBaseController  {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        HttpSession s = request.getSession(false);
        
        
        //caso in cui stiamo svolgendo la ricerca da un utente loggato
        if(s!=null){
        try {
            int IDcollezionista = (int)s.getAttribute("id");
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
                //caricamento sideBar nel caso in cui l'utente abbia effettuato il login
                Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
                List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                dataM.put("collezioni",collezioni);
                

            if(request.getParameter("collezioneK")!=null){
                // per visualizzare dischi esterni
               
                Collezione collezione = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(Integer.parseInt(request.getParameter("collezioneK")));
                Collezionista coll = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaByCollezione(collezione);
                List<Disco> dischiList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDiscoByCollezione(collezione);
                 // trovo gli artisti per ogni disco  per poi aggiungere una lista al data model
                List<Artista> artistiList = new ArrayList();
                for(Disco d : dischiList){
                    artistiList.add(((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaByDisco(d));
                }
                
                // trovo le copie per ogni disco per poi aggiungere una lista al data model
               
                List<List<CopieStato>> csList = new ArrayList();
                List<CopieStato> tempList = new ArrayList();
                for (Disco d : dischiList){
                    tempList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, coll);
                    csList.add(tempList);
                }
               
                dataM.put("csList",csList);
                dataM.put("artistiList", artistiList);
                dataM.put("dischiList",dischiList);
                dataM.put("numero",13);
                t.process(dataM,response.getWriter());
            }else if(request.getParameter("discoK")!=null){
                // per visualizzare tracce esterne

                Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco(Integer.parseInt(request.getParameter("discoK")));
                List<Traccia> tracceList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getTracciaDAO().getTracceByDisco(disco);
                dataM.put("tracceList",tracceList);
                dataM.put("numero",12);
                t.process(dataM,response.getWriter());
            }else if(request.getParameter("cPar")!=null){
                //per visualizzare collezioni esterne
                Collezionista collezioN = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(Integer.parseInt(request.getParameter("cPar")));
                //l'ultimo metodo dao è da modificare
                List<Collezione> collezioniList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioniAccessibili(collezioN,collezionista);
                //
                dataM.put("collezionista",collezioN);
                dataM.put("numero",14);
                dataM.put("collezioniList",collezioniList);
                t.process(dataM,response.getWriter());
            }
            else{
                //caso in cui l'utente effettua una ricerca globale
                
                 String inputDaCercare = request.getParameter("inputDiRicerca");
            
                if(  !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":U"))&&
                    !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":C"))&&
                    !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":D"))&&
                        !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":A"))
                    ){
                    // assegno un tipo di ricerca di default nel caso in cui l'utente non abbia scelto i suggerimenti 
                    inputDaCercare = inputDaCercare+":C";
                }
                 String inputSenzaPlaceH = inputDaCercare.substring(0,inputDaCercare.length()-2);
                 
                 if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":U")){
                     Collezionista collez = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaByNickname(inputSenzaPlaceH);
                     dataM.put("numero", 15);
                     dataM.put("cPar", collez);
                     t.process(dataM,response.getWriter());
                 }else if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":C")){
                    List<Collezione> collezioniList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioniAccessibiliByNome(inputSenzaPlaceH, collezionista);
                    if(collezioniList.size()==0){
                        dataM.put("error",1);
                    }
                    dataM.put("numero",14);
                     dataM.put("collezioniList",collezioniList);
                      t.process(dataM,response.getWriter());
                 }else if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":D")){
                     List<Disco> dischiList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDischiByNome(inputSenzaPlaceH);
                      // trovo gli artisti per ogni disco  per poi aggiungere una lista al data model
                        List<Artista> artistiList = new ArrayList();
                        for(Disco d : dischiList){
                            artistiList.add(((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaByDisco(d));
                        }

                        // trovo le copie per ogni disco per poi aggiungere una lista al data model
                        List<Collezionista> ownersList = new ArrayList();
                        List<List<CopieStato>> csList = new ArrayList();
                        List<CopieStato> tempList = new ArrayList();
                        for (Disco d : dischiList){
                            Collezionista coll = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaByDisco(d);
                            ownersList.add(coll);
                            tempList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, coll);//ci va coll e non null
                            csList.add(tempList);
                        }
                        
                     dataM.put("ownersList",ownersList);   
                     dataM.put("csList",csList);
                     dataM.put("artistiList", artistiList);
                     dataM.put("numero",13);
                     dataM.put("dischiList",dischiList);
                      t.process(dataM,response.getWriter());
                 }else if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":A")){
                     Artista art = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaNomeDarte(inputSenzaPlaceH);
                     if(art!=null){
                         out.println("prima di getDischiByArtista");
                         List<Disco> dischiList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDischiByArtista(art);
                         out.println("dopo di getDischiByArtista");
                         dataM.put("dischiList",dischiList);
                          // trovo gli artisti per ogni disco  per poi aggiungere una lista al data model
                        List<Artista> artistiList = new ArrayList();
                        for(Disco d : dischiList){
                            artistiList.add(((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaByDisco(d));
                        }

                        // trovo le copie per ogni disco per poi aggiungere una lista al data model
                         List<Collezionista> ownersList = new ArrayList();
                        List<List<CopieStato>> csList = new ArrayList();
                        List<CopieStato> tempList = new ArrayList();
                        for (Disco d : dischiList){
                           Collezionista coll = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaByDisco(d);
                           ownersList.add(coll); 
                           tempList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, coll);
                            csList.add(tempList);
                        }
                        
                        dataM.put("ownersList",ownersList);
                        dataM.put("csList",csList);
                        dataM.put("artistiList", artistiList);
                         
                         
                     }
                     else{
                         List<Disco> emptyList = new ArrayList();
                         dataM.put("dischiList",emptyList);
                         dataM.put("errorA",1);
                     }
                    dataM.put("numero",13);
                    t.process(dataM,response.getWriter());
                    }
                }
            } catch (ParseException ex) {
                Logger.getLogger(ServletDiProvaRicercaGlobale.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServletDiProvaRicercaGlobale.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DataException ex) {
                Logger.getLogger(ServletDiProvaRicercaGlobale.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TemplateException ex) {
                Logger.getLogger(ServletDiProvaRicercaGlobale.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
         //caso in cui stiamo svolgendo la ricerca da un utente loggato
        else{
            try {
                ProvaConfig pcg = new ProvaConfig(getServletContext());
                Template t = pcg.getTemplate("dispatcherUnlogged.ftl.html");
                Map<String,Object> dataM = new HashMap();
            
                if(request.getParameter("collezioneK")!=null){
                    // per visualizzare dischi esterni

                    Collezione collezione = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(Integer.parseInt(request.getParameter("collezioneK")));
                    Collezionista coll = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaByCollezione(collezione);
                    List<Disco> dischiList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDiscoByCollezione(collezione);
                     // trovo gli artisti per ogni disco  per poi aggiungere una lista al data model
                    List<Artista> artistiList = new ArrayList();
                    for(Disco d : dischiList){
                        artistiList.add(((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaByDisco(d));
                    }

                    // trovo le copie per ogni disco per poi aggiungere una lista al data model

                    List<List<CopieStato>> csList = new ArrayList();
                    List<CopieStato> tempList = new ArrayList();
                    for (Disco d : dischiList){
                        tempList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, coll);
                        csList.add(tempList);
                    }

                    dataM.put("csList",csList);
                    dataM.put("artistiList", artistiList);
                    dataM.put("dischiList",dischiList);
                    dataM.put("numero",3);
                    t.process(dataM,response.getWriter());
                    }else if(request.getParameter("discoK")!=null){
                    // per visualizzare tracce esterne

                    Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco(Integer.parseInt(request.getParameter("discoK")));
                    List<Traccia> tracceList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getTracciaDAO().getTracceByDisco(disco);
                    dataM.put("tracceList",tracceList);
                    dataM.put("numero",4);
                    t.process(dataM,response.getWriter());
                    }else if(request.getParameter("cPar")!=null){
                    //per visualizzare collezioni esterne
                    Collezionista collezioN = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(Integer.parseInt(request.getParameter("cPar")));
                    List<Collezione> collezioniList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioniAccessibili(collezioN,null);
                    dataM.put("collezionista",collezioN);
                    dataM.put("numero",1);
                    dataM.put("collezioniList",collezioniList);
                    t.process(dataM,response.getWriter());
                }else{
                    //caso in cui l'utente effettua una ricerca globale

                     String inputDaCercare = request.getParameter("inputDiRicerca");

                    if(  !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":U"))&&
                        !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":C"))&&
                        !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":D"))&&
                            !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":A"))
                        ){
                        // assegno un tipo di ricerca di default nel caso in cui l'utente non abbia scelto i suggerimenti 
                        inputDaCercare = inputDaCercare+":C";
                    }
                     String inputSenzaPlaceH = inputDaCercare.substring(0,inputDaCercare.length()-2);

                     if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":U")){
                         Collezionista collez = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaByNickname(inputSenzaPlaceH);
                         dataM.put("numero", 2);
                         dataM.put("cPar", collez);
                         t.process(dataM,response.getWriter());
                     }else if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":C")){
                        List<Collezione> collezioniList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioniAccessibiliByNome(inputSenzaPlaceH, null);
                        if(collezioniList.size()==0){
                            dataM.put("error",1);
                         } 
                        dataM.put("numero",1);
                         dataM.put("collezioniList",collezioniList);
                          t.process(dataM,response.getWriter());
                     }else if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":D")){
                         List<Disco> dischiList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDischiByNome(inputSenzaPlaceH);
                         // trovo gli artisti per ogni disco  per poi aggiungere una lista al data model
                        List<Artista> artistiList = new ArrayList();
                        for(Disco d : dischiList){
                            artistiList.add(((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaByDisco(d));
                        }

                        // trovo le copie per ogni disco per poi aggiungere una lista al data model
                         List<Collezionista> ownersList = new ArrayList();
                        List<List<CopieStato>> csList = new ArrayList();
                        List<CopieStato> tempList = new ArrayList();
                        for (Disco d : dischiList){
                            Collezionista coll = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaByDisco(d);
                            ownersList.add(coll);
                            tempList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, coll);
                            csList.add(tempList);
                        }
                        dataM.put("ownersList",ownersList);
                        dataM.put("csList",csList);
                        dataM.put("artistiList", artistiList);
                        dataM.put("numero",3);
                        dataM.put("dischiList",dischiList);
                          t.process(dataM,response.getWriter());
                     }else if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":A")){
                         Artista art = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaNomeDarte(inputSenzaPlaceH);
                         if(art!=null){
                             List<Disco> dischiList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDischiByArtista(art);
                             dataM.put("dischiList",dischiList);
                              // trovo gli artisti per ogni disco  per poi aggiungere una lista al data model
                        List<Artista> artistiList = new ArrayList();
                        for(Disco d : dischiList){
                            artistiList.add(((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaByDisco(d));
                        }

                        // trovo le copie per ogni disco per poi aggiungere una lista al data model
                        List<Collezionista> ownersList = new ArrayList();
                        List<List<CopieStato>> csList = new ArrayList();
                        List<CopieStato> tempList = new ArrayList();
                        for (Disco d : dischiList){
                            Collezionista coll = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaByDisco(d);
                             ownersList.add(coll);
                            tempList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, coll);
                            csList.add(tempList);
                        }
                        dataM.put("ownersList", ownersList);
                        dataM.put("csList",csList);
                        dataM.put("artistiList", artistiList);
                         }
                         else{
                             List<Disco> emptyList = new ArrayList();
                             dataM.put("dischiList",emptyList);
                             dataM.put("errorA",1);
                         }
                        dataM.put("numero",3);
                        t.process(dataM,response.getWriter());
                     }
                }
            

            } catch (ParseException ex) {
                Logger.getLogger(ServletDiProvaRicercaGlobale.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServletDiProvaRicercaGlobale.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DataException ex) {
                Logger.getLogger(ServletDiProvaRicercaGlobale.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TemplateException ex) {
                Logger.getLogger(ServletDiProvaRicercaGlobale.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
    }

   
}
