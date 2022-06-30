/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.ArtistaImpl;
import collector_site.data.impl.DiscoImpl;
import collector_site.data.impl.Genere;
import collector_site.data.impl.Ruolo;
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
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class ServletDiProvaInserisciDisco extends ServletDiProvaCollector_siteBaseController  {

    //advice_disco(request,response,IDcollezionista,s,IDdisco);
    private void advice_disco(HttpServletRequest request, HttpServletResponse response, int IDcollezionista,HttpSession s,int IDdisco) throws DataException, IOException{
        
        int idCollezione = Integer.parseInt((String) s.getAttribute("IDCollezioneSessione"));
        Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco(IDdisco);
        ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().addDiscoToCollezione(disco, 
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(
                            idCollezione));
        
         s.removeAttribute("IDCollezioneSessione");
                response.sendRedirect("servletDiProvaVistaCollezione?k="+idCollezione);
        
        
    }
    
    
    
    private void store_DG(HttpServletRequest request, HttpServletResponse response, int IDcollezionista,HttpSession s) throws DataException, IOException{
        
        out.println("porco e fagioli0");
        int idCollezione = Integer.parseInt((String) s.getAttribute("IDCollezioneSessione"));
        out.println("porco e fagioli1");
        Disco d = (Disco) s.getAttribute("discoSessione");
        out.println("porco e fagioli2");
        Artista g = (Artista) s.getAttribute("gruppoSessione");
        out.println("porco e fagioli3");
        Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
        out.println("porco e fagioli4");
        List<Artista> listA = (List<Artista>) s.getAttribute("ListaArtisti");
        out.println("porco e fagioli5");
        
        Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().createDisco();
        disco.setTipo(d.getTipo());
        disco.setGenere(d.getGenere());
        disco.setAnno(d.getAnno());
        disco.setBarcode(d.getBarcode());
        disco.setCollezionista(collezionista);
        disco.setEtichetta(d.getBarcode());
        disco.setNomeDisco(d.getNomeDisco());
        out.println("porco e fagioli6");
        
        Artista gruppo = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().createArtista();
        gruppo.setComponenti(listA);
        gruppo.setNomeDarte(g.getNomeDarte());
        out.println("porco e fagioli7");
                
        ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().storeDisco(disco);
        out.println("porco e fagioli8");
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().storeArtista(gruppo);
                out.println("porco e fagioli9");
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().setArtistaOfDisco(disco, gruppo);
                out.println("porco e fagioli10");
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().addDiscoToCollezione(disco, 
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(
                            idCollezione));
                out.println("porco e fagioli11");
                
                s.removeAttribute("gruppoSessione");
                s.removeAttribute("ListaArtisti");
                s.removeAttribute("discoSessione");
                s.removeAttribute("IDCollezioneSessione");
                response.sendRedirect("servletDiProvaVistaCollezione?k="+idCollezione);
                //s.removeAttribute("ListaArtisti");
        
    }
    
    
    
    private void create_artista(HttpServletRequest request, HttpServletResponse response, Map<String,Object> dataM, int IDcollezionista,HttpSession s){
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
               
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().storeDisco(disco);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().storeArtista(artista);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().setArtistaOfDisco(disco, artista);
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().addDiscoToCollezione(disco, 
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(
                            idCollezione));
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

                // se il gruppo esiste già 
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
                    gruppo.setNomeDarte(nomeGruppoPar);
                    
                    
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
                    
                    gruppo.setNomeDarte(nomeArtistaPar);
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
            
            Disco disco = new DiscoImpl();
            disco.setTipo(Tipo.valueOf(tipoDiscoPar[0]));
            disco.setGenere(Genere.valueOf(genereDiscoPar[0]));
            disco.setAnno((int)Integer.parseInt(datePar));
            disco.setBarcode(barcodePar);
            disco.setCollezionista(collezionista);
            disco.setEtichetta(etichettaPar);
            disco.setNomeDisco(nomeDiscoPar);
            
            
            
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
                 out.println("porco e faggi");
                 //significa che chiamo la servlet per lo storage di un gruppo musicale e del disco che hanno inciso
                 store_DG(request,response,IDcollezionista,s);
             }else if(request.getParameter("AdviceD")!=null){
                 int IDdisco = Integer.parseInt(request.getParameter("AdviceD"));
                 advice_disco(request,response,IDcollezionista,s,IDdisco);
             }
             else{
                 //significa che chiamo la servlet per l'inserimento e lo storage di un singolo artista/ per l'inserimento di un gruppo
                 create_artista(request,response,dataM,IDcollezionista,s);
             }} catch (ParseException ex) {
             Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException | TemplateException | DataException | java.text.ParseException ex) {
             Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
         }
       
        
    }
    
    
    

}
