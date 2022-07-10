    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.DAO.CollezionistaDAO;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.framework.data.DataException;
import collector_site.framework.result.ProvaConfig;
import freemarker.core.ParseException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class ServletDiProvaLogin extends ServletDiProvaCollector_siteBaseController {
    
    
    private void login_action(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataException{
        String nickname = request.getParameter("fname");
        String password = request.getParameter("lname");
        Integer IDutente = null;
        
        IDutente = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().login(nickname,password);
        
       
           
        if (IDutente != null && IDutente >= 1) { // se l'autenticazione è andata a buon fine
            // creazione della sessione utente
            HttpSession s = request.getSession(true);
            s.setAttribute("id", IDutente);
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
            try {
                // estraggo le collezioni dal collezionista che ha effettuato il login
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
                    Logger.getLogger(ServletDiProvaLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            else {                                                         //se l'autenticazione è fallita
                ProvaConfig pcg = new ProvaConfig(getServletContext());
                Template t = pcg.getTemplate("login.ftl.html");
                Map<String,Object> dataM = new HashMap();
                dataM.put("numero",0);
                dataM.put("errore",1);
                try {
                    t.process(dataM,response.getWriter());
                } catch (TemplateException ex) {
                    Logger.getLogger(ServletDiProvaLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   
    }
   
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        if(request.getParameter("fname")==null){            // se la servlet viene chiamata da una pagina senza sessione
            try {
                ProvaConfig pcg = new ProvaConfig(getServletContext());
                Template t = pcg.getTemplate("login.ftl.html");
                Map<String,Object> dataM = new HashMap();
                dataM.put("numero",0);
                dataM.put("errore",0);
                try {
                    t.process(dataM,response.getWriter());
                } catch (TemplateException ex) {
                    Logger.getLogger(ServletDiProvaLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (ParseException ex) {
                Logger.getLogger(ServletDiProvaLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServletDiProvaLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{     // caso in cui si preme il tasto login dalla schermata login.ftl.html
            try {
                login_action(request,response);
            } catch (IOException ex) {
                Logger.getLogger(ServletDiProvaLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DataException ex) {
                Logger.getLogger(ServletDiProvaLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}


