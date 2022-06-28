/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.ArtistaImpl;
import collector_site.data.impl.DiscoImpl;
import collector_site.data.impl.Genere;
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

    private void create_artista(HttpServletRequest request, HttpServletResponse response, Map<String,Object> dataM, int IDcollezionista,HttpSession s){
        try {
            
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            
            //completo la sideBar con la lista di collezioni
            Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
            List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
            dataM.put("collezioni",collezioni);
            
            
            Disco disco = (Disco) s.getAttribute("discoSessione");
            out.println(disco.getNomeDisco());
            dataM.put("disco",disco);
            dataM.put("numero",8);
            t.process(dataM, response.getWriter());
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
            out.println("dioPorcone1");
            String barcodePar = request.getParameter("barcodePar");//
            out.println("dioPorcone2");
            String[] tipoDiscoPar = request.getParameterValues("tipoDiscoPar");//
            out.println("dioPorcone3");
            String datePar = request.getParameter("datePar"); // 
            out.println("dioPorcone5");
            String etichettaPar = request.getParameter("etichettaPar");//
            out.println("dioPorcone6");
            String[] genereDiscoPar = request.getParameterValues("genereDiscoPar");//
            out.println("dioPorcone7");
            String nomeArtistaPar = request.getParameter("nomeArtistaPar");
            out.println("dioPorcone8");
            
            Disco disco = new DiscoImpl();
            disco.setTipo(Tipo.CD);
            disco.setGenere(Genere.Dance);
            disco.setAnno(1948);
            disco.setBarcode(barcodePar);
            disco.setCollezionista(collezionista);
            disco.setEtichetta(etichettaPar);
            disco.setNomeDisco(nomeDiscoPar);
            
            
            out.println("dioPorcone4");
            s.setAttribute("discoSessione", disco);
            out.println("dioPorcone5");
            
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
             if(request.getParameter("inserisciD")!=null){
                 //significa che ho chiamato la servlet per l'inserimento di un nuovo disco dalla vista di una collezione
                 
                 //completo la sideBar con la lista di collezioni
                 Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
                 List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                 dataM.put("collezioni",collezioni);
                 
                 dataM.put("numero",2);
                 t.process(dataM, response.getWriter());
             }else if(request.getParameter("etichettaPar")!=null){
                //significa che ho chiamato la servlet per l'inserimento di un artista dopo aver inserito un disco
                 add_disco(request,response,dataM,IDcollezionista,s);// chiama la funzione di storage di un disco
             }
             else{
                 create_artista(request,response,dataM,IDcollezionista,s);
             }} catch (ParseException ex) {
             Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException | TemplateException | DataException | java.text.ParseException ex) {
             Logger.getLogger(ServletDiProvaInserisciDisco.class.getName()).log(Level.SEVERE, null, ex);
         }
       
        
    }
    
    
    

}
