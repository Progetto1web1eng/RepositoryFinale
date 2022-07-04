/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.DiscoImpl;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
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
public class ServletDiProvaVistaCollezione extends ServletDiProvaCollector_siteBaseController {

     @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        try {
            
            HttpSession s = request.getSession(false);
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
            
            String idcollS = request.getParameter("k");
            out.println("id collezione:"+idcollS);
            int idColl = Integer.parseInt(idcollS);
            
            try {        // estraggo le collezioni dal collezionista che ha effettuato il login per la visualizzazione del side menu
                Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById((int)s.getAttribute("id"));
                List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                dataM.put("collezioni",collezioni);
                        // estraggo la lista di dischi data una collezione
                        
                out.println("ciao1");
                Collezione collezioneSelezionata = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(idColl);
                 out.println("ciao2");
                List<Disco> dischiList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDiscoByCollezione(collezioneSelezionata);
                 out.println("ciao3");
                Disco disco =((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco((int)s.getAttribute("id"));
                List<Immagine>immagini = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getImmagineDAO().getImmaginiByDisco(disco);
              
                dataM.put("immagini",immagini);
                dataM.put("collezioneSelezionata",collezioneSelezionata);
                dataM.put("dischiList",dischiList);
                 out.println("ciao4");
            
            } catch (DataException ex) {
                Logger.getLogger(ServletDiProvaLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            dataM.put("numero",3);
            
            try {
                t.process(dataM,response.getWriter());
                
            } catch (TemplateException ex) {
                Logger.getLogger(ServletDiProvaVistaCollezione.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaVistaCollezione.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
             Logger.getLogger(ServletDiProvaVistaCollezione.class.getName()).log(Level.SEVERE, null, ex);
         }
        
        
        
    }

}
