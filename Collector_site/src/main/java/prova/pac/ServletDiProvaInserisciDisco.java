/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.ArtistaImpl;
import collector_site.data.impl.CopieStato;
import collector_site.data.impl.DiscoImpl;
import collector_site.data.impl.Genere;
import collector_site.data.impl.Ruolo;
import collector_site.data.impl.StatoDisco;
import collector_site.data.impl.Tipo;
import collector_site.data.model.Artista;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.framework.data.DataException;
import collector_site.framework.result.ProvaConfig;
import freemarker.core.HTMLOutputFormat;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author fabri
 */
public class ServletDiProvaInserisciDisco extends ServletDiProvaCollector_siteBaseController  {

    private void advice_artista(HttpServletRequest request, HttpServletResponse response,HttpSession s,int IDartista) throws DataException, IOException{
        int idCollezione = Integer.parseInt((String) s.getAttribute("IDCollezioneSessione"));
        Disco disco = (Disco) s.getAttribute("discoSessione");
        Artista artista = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaById(IDartista);
        
        ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().storeDisco(disco);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().storeArtista(artista);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().setArtistaOfDisco(disco, artista);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().addDiscoToCollezione(disco, 
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(
                            idCollezione));
        
        
                
        response.sendRedirect("servletDiProvaVistaCollezione?k="+idCollezione);
    }   
    
    
    private void advice_disco(HttpServletRequest request, HttpServletResponse response, int IDcollezionista,HttpSession s,int IDdisco,Template t,Map<String,Object> dataM) throws DataException, IOException{
        
        
        try {
            int idCollezione = Integer.parseInt((String) s.getAttribute("IDCollezioneSessione"));
            Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco(IDdisco); 
            ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().addDiscoToCollezione(disco,
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(
                            idCollezione));
            
            dataM.put("numero",9);
            t.process(dataM, response.getWriter());
            
            //response.sendRedirect("servletDiProvaVistaCollezione?k="+idCollezione);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    
    private void store_DG(HttpServletRequest request, HttpServletResponse response, int IDcollezionista,HttpSession s) throws DataException, IOException{
       
        int idCollezione = Integer.parseInt((String) s.getAttribute("IDCollezioneSessione"));
        Disco d = (Disco) s.getAttribute("discoSessione");
        Artista g = (Artista) s.getAttribute("gruppoSessione");
        Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
        List<Artista> listA = (List<Artista>) s.getAttribute("ListaArtisti");
        
        Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().createDisco();
        disco.setTipo(d.getTipo());
        disco.setGenere(d.getGenere());
        disco.setAnno(d.getAnno());
        disco.setBarcode(d.getBarcode());
        disco.setCollezionista(collezionista);
        disco.setEtichetta(d.getBarcode());
        disco.setNomeDisco(d.getNomeDisco());
        
        Artista gruppo = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().createArtista();
        gruppo.setComponenti(listA);
        gruppo.setNomeDarte(g.getNomeDarte());
        
                
        ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().storeDisco(disco);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().storeArtista(gruppo);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().setArtistaOfDisco(disco, gruppo);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().addDiscoToCollezione(disco, 
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(
                            idCollezione));
                
                s.removeAttribute("gruppoSessione");
                s.removeAttribute("ListaArtisti");
                s.removeAttribute("discoSessione");
                s.removeAttribute("IDCollezioneSessione");
                response.sendRedirect("servletDiProvaVistaCollezione?k="+idCollezione);
                //s.removeAttribute("ListaArtisti");
        
    }
    
    
    
    private void create_artista(HttpServletRequest request, HttpServletResponse response, Map<String,Object> dataM, int IDcollezionista,HttpSession s) throws DataException, org.json.simple.parser.ParseException, FileNotFoundException{
        try {
            
            
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            
            //completo la sideBar con la lista di collezioni
            Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
            List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
            dataM.put("collezioni",collezioni);
            
            
            if(request.getParameter("singoloAPar")!=null){
                // caso in cui stiamo inserendo un singolo artista
                String nomeArt = request.getParameter("singoloAPar");
                Disco d = (Disco) s.getAttribute("discoSessione");
                int idCollezione = Integer.parseInt((String) s.getAttribute("IDCollezioneSessione"));
                

                List<Disco> listaTempD = new ArrayList();
                List<Artista> listaTempA = new ArrayList();
                

                Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().createDisco();
                disco.setTipo(d.getTipo());
                disco.setGenere(d.getGenere());
                disco.setAnno(d.getAnno());
                disco.setBarcode(d.getBarcode());
                disco.setCollezionista(collezionista);
                disco.setEtichetta(d.getBarcode());
                disco.setNomeDisco(d.getNomeDisco());
                listaTempD.add(d);

                Artista artista = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().createArtista();
                artista.setNomeDarte(nomeArt);
                artista.setDischiIncisi(listaTempD);
                listaTempA.add(artista);
               
                
                CopieStato cp = (CopieStato) s.getAttribute("copieStato");
                List<CopieStato> listcp = new ArrayList();
                listcp.add(cp);
                disco.setCopieStati(listcp);
                
                out.println(cp.getStato().toString());
                out.println(cp.getNumCopieDisco());
                
                
                
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().addDiscoToCollezionista(disco, collezionista);
                out.println("porco e fagioli1");
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().updateQuantitaDisco(disco, collezionista,cp.getStato(),cp.getNumCopieDisco());
                out.println("porco e fagioli2");
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().storeDisco(disco);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().storeArtista(artista);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().setArtistaOfDisco(disco, artista);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().addDiscoToCollezione(disco, 
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(
                            idCollezione));
                
                
                //storage dei dischi nel file json
                /*
                try (FileWriter file = new FileWriter("script/dischi.json")){
                    out.println("bruttamadonna");
                    JSONParser jsonParser = new JSONParser();
                    out.println("bruttamadonna");
                    try (FileReader reader = new FileReader("dischi.json")){
                        out.println("bruttamadonna");
                    Object obj = jsonParser.parse(reader);
                            out.println("bruttamadonna");
                        JSONArray dischiList = (JSONArray) obj;
                        out.println("bruttamadonna");
                        for(Object j : dischiList){
                            out.println("porco e fagioli");
                        }
                    }
                    
                    String nomeDisco;
                    int ID;
                    JSONObject newJSON = new JSONObject();
                    for(Disco disk : ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDischi()){
                        nomeDisco = disk.getNomeDisco();
                        ID = disk.getKey();
                        newJSON.put("nomeDisco", nomeDisco);
                        newJSON.put("ID",ID);
                    }
                    file.write(newJSON.toJSONString());
                    file.flush();
                }*/
                
                
                s.removeAttribute("gruppoSessione");
                s.removeAttribute("ListaArtisti");
                s.removeAttribute("discoSessione");
                s.removeAttribute("IDCollezioneSessione");
                
                response.sendRedirect("servletDiProvaVistaCollezione?k="+idCollezione);
                
                
                // caso in cui stiamo inserendo un gruppo
            }else{
                Disco disco = (Disco) s.getAttribute("discoSessione");
                String nomeGruppoPar = request.getParameter("nomeGruppoPar");
                String nomeArtistaPar = request.getParameter("nomeArtistaPar");
                String[] ruoloPar = request.getParameterValues("ruoloPar");

                // se il gruppo esiste gi� 
                if(s.getAttribute("gruppoSessione")!=null){
                    Artista gruppo = (Artista) s.getAttribute("gruppoSessione");
                    Artista art = new ArtistaImpl();
                    
                    List<Artista> listaTempA = gruppo.getComponenti();
                    List<Artista> listaTempG = new ArrayList();
                    
                    listaTempG.add(gruppo);
                    art.setComponenti(listaTempG);
                    art.setNomeDarte(nomeArtistaPar);
                    art.setRuolo(Ruolo.valueOf(ruoloPar[0]));
                    listaTempA.add(art);
                    gruppo.setComponenti(listaTempA);
                    
                    
                    s.setAttribute("gruppoSessione", gruppo);
                    s.setAttribute("ListaArtisti", gruppo.getComponenti());
                    
                    dataM.put("numero",8);
                    dataM.put("nomeGruppo",gruppo.getNomeDarte());
                    dataM.put("listaArtisti", gruppo.getComponenti());
                    dataM.put("ripetizioni", 1);
                    t.process(dataM, response.getWriter());
                     
                // se il gruppo non esiste  
                }else{
                    Artista gruppo = new ArtistaImpl();
                    Artista art = new ArtistaImpl();
                    
                    List<Artista> listaTempA = new ArrayList();
                    List<Artista> listaTempG = new ArrayList();
                    List<Disco>   listaTempD = new ArrayList();
                    
                    gruppo.setNomeDarte(nomeGruppoPar);
                    art.setNomeDarte(nomeArtistaPar);
                    art.setRuolo(Ruolo.valueOf(ruoloPar[0]));
                    
                    listaTempA.add(art);
                    gruppo.setComponenti(listaTempA);
                    
                    listaTempD.add(disco);
                    gruppo.setDischiIncisi(listaTempD);
                    
                    listaTempG.add(gruppo);
                    art.setComponenti(listaTempG);
                    
                    s.setAttribute("gruppoSessione", gruppo);
                    s.setAttribute("ListaArtisti", gruppo.getComponenti());
                    s.setAttribute("discoSessione", disco);
                    
                    dataM.put("numero",8);
                    dataM.put("nomeGruppo",gruppo.getNomeDarte());
                    dataM.put("listaArtisti", gruppo.getComponenti());
                    dataM.put("ripetizioni", 1);
                    t.process(dataM, response.getWriter());
   
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void add_disco(HttpServletRequest request, HttpServletResponse response, Map<String,Object> dataM, int IDcollezionista,HttpSession s) throws java.text.ParseException, DataException{
        try {
            
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            
            //completo la sideBar con la lista di collezioni
            Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
            List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
            dataM.put("collezioni",collezioni);
            
            String nomeDiscoPar =request.getParameter("nomeDiscoPar");//
            String barcodePar = request.getParameter("barcodePar");//
            String[] tipoDiscoPar = request.getParameterValues("tipoDiscoPar");//
            String datePar = request.getParameter("dataPar"); // 
            String etichettaPar = request.getParameter("etichettaPar");//
            String[] genereDiscoPar = request.getParameterValues("genereDiscoPar");//
            String[] statoDiscoPar = request.getParameterValues("statoDiscoPar");
            String numeroDiCopiePar = request.getParameter("numeroDiCopiePar");
            
            CopieStato copieStato = new CopieStato();
            copieStato.setNumCopieDisco(Integer.parseInt(numeroDiCopiePar));
            copieStato.setStato(StatoDisco.valueOf(statoDiscoPar[0]));
            
            
            Disco disco = new DiscoImpl();
            disco.setTipo(Tipo.valueOf(tipoDiscoPar[0]));
            disco.setGenere(Genere.valueOf(genereDiscoPar[0]));
            disco.setAnno((int)Integer.parseInt(datePar));
            disco.setBarcode(barcodePar);
            disco.setCollezionista(collezionista);
            disco.setEtichetta(etichettaPar);
            disco.setNomeDisco(nomeDiscoPar);
            
            
            s.setAttribute("copieStato", copieStato);
            s.setAttribute("discoSessione", disco);
           
            dataM.put("ripetizioni",0);
            dataM.put("numero",8);
            
            t.process(dataM, response.getWriter());
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
         try {
            
             HttpSession s = request.getSession(false);
             
             
             int IDcollezionista = (int)s.getAttribute("id");
             ProvaConfig pcg = new ProvaConfig(getServletContext());
             Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
             Map<String,Object> dataM = new HashMap();
             if(request.getParameter("collezioneKey")!=null){
                 
                 // rimuovo eventuali attributi di sessione
                s.removeAttribute("gruppoSessione");
                s.removeAttribute("ListaArtisti");
                s.removeAttribute("discoSessione");
                s.removeAttribute("IDCollezioneSessione");
                 
                
                 //significa che ho chiamato la servlet per l'inserimento di un nuovo disco dalla vista di una collezione
                 s.setAttribute("IDCollezioneSessione", request.getParameter("collezioneKey"));
                 //completo la sideBar con la lista di collezioni
                 
                 Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
                 List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                 dataM.put("collezioni",collezioni);
                 
                 dataM.put("numero",2);
                 t.process(dataM, response.getWriter());
             }else if(request.getParameter("etichettaPar")!=null){
                //significa che ho chiamato la servlet per l'inserimento di un artista dopo aver inserito un disco
                 add_disco(request,response,dataM,IDcollezionista,s);// chiama la funzione di storage di un disco
             }else if(request.getParameter("insDG")!=null){
                 //significa che chiamo la servlet per lo storage di un gruppo musicale e del disco che hanno inciso
                 store_DG(request,response,IDcollezionista,s);
             }else if(request.getParameter("AdviceD")!=null){
                 //significa che ho selezionato un disco gi� esistente
                 int IDdisco = Integer.parseInt(request.getParameter("AdviceD"));
                 advice_disco(request,response,IDcollezionista,s,IDdisco,t,dataM);
             }else if (request.getParameter("AdviceA")!=null){
                 // significa che sto selezionando un artista/gruppo gi� esistente
                 int IDartista = Integer.parseInt(request.getParameter("AdviceA"));
                 advice_artista(request,response,s,IDartista);
             }/*else if(request.getParameter("settingCS")!=null){
                 
                 set_copieStato
             }*/
             else{
                 //significa che chiamo la servlet per l'inserimento e lo storage di un singolo artista/ per l'inserimento di un gruppo
                 create_artista(request,response,dataM,IDcollezionista,s);
             }} catch (ParseException ex) {
             Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException | TemplateException | DataException | java.text.ParseException ex) {
             Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
         } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
    }
    
    
    

}
