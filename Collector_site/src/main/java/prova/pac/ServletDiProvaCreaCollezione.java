/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
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
public class ServletDiProvaCreaCollezione extends ServletDiProvaCollector_siteBaseController {
    
    private void aggiungi_action(HttpServletRequest request, HttpServletResponse response, Map<String,Object> dataM, int IDcollezionista) throws ServletException, DataException, TemplateException{
       
        try {
            dataM.put("numero",1);
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            //completo la sideBar con la lista di collezioni
            Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
            List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
            dataM.put("collezioni",collezioni);
            
            
            // creo la collezione dall'input 
            Collezione collezione = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().createCollezione();
           
            
            
                String[] valC = request.getParameterValues("checkbox");
                String nomeC = request.getParameter("nomeColl");
                dataM.put("errore", 0);
                // se i dati sono stati inseriti correttamente
                if (valC[0].equals("Privata")){
                    collezione.setCreatore(collezionista);
                    collezione.setNomeCollezione(nomeC);
                    collezione.setPubblico(false);

                    //aggiungo la collezione al db
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().storeCollezione(collezione);

                    //aggiorno il side menu
                    dataM.remove("collezioni");
                    List<Collezione> collezion = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                    dataM.put("collezioni",collezion);

                }else{
                    collezione.setCreatore(collezionista);
                    collezione.setNomeCollezione(nomeC);
                    collezione.setPubblico(true);
                    //aggiungo la collezione al db
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().storeCollezione(collezione);

                    //aggiorno il side menu
                    dataM.remove("collezioni");
                    List<Collezione> collezion = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                    dataM.put("collezioni",collezion);
                }
            
            
            t.process(dataM,response.getWriter());
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaCreaCollezione.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaCreaCollezione.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            HttpSession s = request.getSession(false);
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
            int IDcollezionista = (int) s.getAttribute("id");
            if(request.getParameter("crea")==null){
                try {
                     // se il check box non ha valori o non si è inserito un nome alla collezione
                    if((request.getParameterValues("checkbox")==null)||(request.getParameter("nomeColl").length()==0)){
                    dataM.put("errore", 1);
                    dataM.put("numero",1);
                    //completo la sideBar con la lista di collezioni
                    Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
                    List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                    dataM.put("collezioni",collezioni);
                    t.process(dataM, response.getWriter());
                    }
                    // se il checkbox ha due valori
                    else if(request.getParameterValues("checkbox").length==2){
                    dataM.put("errore", 1);
                    dataM.put("numero",1);
                    //completo la sideBar con la lista di collezioni
                    Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
                    List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                    dataM.put("collezioni",collezioni);
                    t.process(dataM, response.getWriter());
                    }
                    else{
                    // caso in cui i dati sono stati inseriti correttamente
                    aggiungi_action(request,response,dataM,IDcollezionista);
                    }
                } catch (DataException ex) {
                    Logger.getLogger(ServletDiProvaCreaCollezione.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                
                //completo la sideBar con la lista di collezioni
                Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
                List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                dataM.put("collezioni",collezioni);
            
                dataM.put("errore",0);
                dataM.put("numero",1);
                t.process(dataM, response.getWriter());
            }
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaCreaCollezione.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaCreaCollezione.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaCreaCollezione.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaCreaCollezione.class.getName()).log(Level.SEVERE, null, ex);
        }
            
           
    }

}
