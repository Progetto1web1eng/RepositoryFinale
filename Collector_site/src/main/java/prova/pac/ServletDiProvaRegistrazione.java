/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.CollezionistaImpl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fabri
 */
public class ServletDiProvaRegistrazione extends ServletDiProvaCollector_siteBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("login.ftl.html");
            Map<String,Object> dataM = new HashMap();
            if(request.getParameter("r")!=null){
                //per la visualizzazione della schermata di registrazione
                 dataM.put("numero",1);
                t.process(dataM, response.getWriter());
            }else{
                //per registrarsi sul sito
                boolean esiste=false;
                String telefonoInput = request.getParameter("telefonoInput");
                String emailInput = request.getParameter("emailInput");
                String nicknameInput = request.getParameter("nicknameInput");
                String passwordInput = request.getParameter("passwordInput");
                String confermaInput = request.getParameter("confermaInput");
                
                if(telefonoInput.length()!=0&&emailInput.length()!=0&&nicknameInput.length()!=0&&passwordInput.length()!=0&&confermaInput.length()!=0&&
                        passwordInput.equals(confermaInput)){
                    // caso in cui la password e la conferma password combaciano
                    Collezionista c = new CollezionistaImpl();
                    c.setCellulare(telefonoInput);
                    c.setEmail(emailInput);
                    c.setNickname(nicknameInput);
                    c.setPassword(passwordInput);
                    
                    List<Collezionista> listC = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionisti();
                    for(Collezionista coll : listC){
                        if(c.getNickname().equals(coll.getNickname())){
                             esiste=true;
                             break;
                        }
                    }
                    if(esiste){
                        //esiste già un utente con questo nickname
                         dataM.put("numero",1);
                        dataM.put("error",2);
                        t.process(dataM, response.getWriter());
                        
                    }else{
                        // tutto corretto eseguo lo store dei parametri sul db
                         ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().storeCollezionista(c);
                        dataM.put("numero",0);
                        dataM.put("errore",0);
                        t.process(dataM, response.getWriter());
                    }
                    
                }else{
                    //parametri inseriti non corretti
                    dataM.put("numero",1);
                    dataM.put("errore",1);
                     t.process(dataM, response.getWriter());
                }
                
               
            }
           
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaRegistrazione.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaRegistrazione.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaRegistrazione.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaRegistrazione.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  

}
