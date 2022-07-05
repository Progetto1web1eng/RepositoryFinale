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
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import freemarker.core.ParseException;
import freemarker.core.XMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.PrintStream;
import static java.lang.System.console;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import static javax.servlet.jsp.tagext.TagAttributeInfo.ID;

/**
 *
 * @author fabri
 */
public class ServletDiProvaHome extends ServletDiProvaCollector_siteBaseController {

   

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession s = request.getSession(false);
        if(s==null){
             try {
                 ProvaConfig pcg = new ProvaConfig(getServletContext());
                  Map<String,Object> dataM = new HashMap();
                  dataM.put("numero", 0);
                 Template t = pcg.getTemplate("dispatcherUnlogged.ftl.html");
                 try {
                     t.process(dataM,response.getWriter());
                     
                 } catch (TemplateException ex) {
                     Logger.getLogger(ServletDiProvaHome.class.getName()).log(Level.SEVERE, null, ex);
                 }
             } catch (ParseException ex) {
                Logger.getLogger(ServletDiProvaHome.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                 Logger.getLogger(ServletDiProvaHome.class.getName()).log(Level.SEVERE, null, ex);
             }
        }else{
             try {
                 ProvaConfig pcg = new ProvaConfig(getServletContext());
                 Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
                 Map<String,Object> dataM = new HashMap();
                 
                 
                 
                 try {        // estraggo le collezioni dal collezionista che ha effettuato il login
                    
                    Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById((int)s.getAttribute("id"));
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO();
                    List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                    dataM.put("collezioni",collezioni);
                
                } catch (DataException ex) {
                    Logger.getLogger(ServletDiProvaLogin.class.getName()).log(Level.SEVERE, null, ex);
                }  
                 
                dataM.put("numero",6);
                try {
                    t.process(dataM,response.getWriter()); 
                } catch (TemplateException ex) {
                    Logger.getLogger(ServletDiProvaHome.class.getName()).log(Level.SEVERE, null, ex);
                }
             } catch (ParseException ex) {
                Logger.getLogger(ServletDiProvaHome.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                 Logger.getLogger(ServletDiProvaHome.class.getName()).log(Level.SEVERE, null, ex);
             
            }
        }
    }   
}

  




