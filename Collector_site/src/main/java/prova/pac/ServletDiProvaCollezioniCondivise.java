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
import collector_site.framework.utils.SendEmail;
import freemarker.core.ParseException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author fabri
 */
public class ServletDiProvaCollezioniCondivise extends ServletDiProvaCollector_siteBaseController {

    private void delete_condivisione(HttpServletRequest request, HttpServletResponse response,HttpSession s,Collezionista cDaRimuovere) throws DataException, IOException{
        Collezione collez = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById((int) s.getAttribute("collezioneSelezionata"));
                ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().deleteCondivisione(collez, cDaRimuovere);
        response.sendRedirect("servletDiProvaCollezioniCondivise?AggCond="+((int) s.getAttribute("collezioneSelezionata")));
    }
    
    private void aggiungi_condivisione(HttpServletRequest request, HttpServletResponse response,HttpSession s) throws DataException, IOException, MessagingException{
        int e=0;
        String nicknamePar = request.getParameter("nicknamePar");
        Collezione coll = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById((int) s.getAttribute("collezioneSelezionata"));
        Collezionista collDaAggiungere = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaByNickname(nicknamePar);
        if(collDaAggiungere!=null){
            ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().addCondivisione(coll, collDaAggiungere);
        }else{
            e=1;
        }
        // metodo per inviare una mail, questa è solo una simulazione
        SendEmail.sendEmail("37d5cb32ad4102");
        
        
        response.sendRedirect("servletDiProvaCollezioniCondivise?AggCond="+((int) s.getAttribute("collezioneSelezionata")+"&e="+e));

    }
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            HttpSession s = request.getSession(false);
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
            // estraggo le collezioni dal collezionista che ha effettuato il login per la visualizzazione del side menu
            Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById((int)s.getAttribute("id"));
            List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
            dataM.put("collezioni",collezioni);
            
            List<Collezione> collezioniCondivise = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioniPrivateCondiviseToCollezionista(collezionista);
            
            if(request.getParameter("AggCond")!=null){
                //vista di aggiunta di una condivisione la vista ha il numero 16
                Collezione collSelezionata = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(Integer.parseInt(request.getParameter("AggCond")));
                List<Collezionista> condivisioniACollezionisti = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCondivisioniByCollezione(collSelezionata);
                
                if(request.getParameter("e")!=null){
                    if(Integer.parseInt(request.getParameter("e"))==1){
                         dataM.put("errore",1);
                    }
                }
                
                dataM.put("numero",16);
                dataM.put("collezione",collSelezionata);
                dataM.put("collezionistiList",condivisioniACollezionisti);
                 t.process(dataM, response.getWriter());
                // devo ricavare una lista di condivisioni
            }else if(request.getParameter("nicknamePar")!=null){
                aggiungi_condivisione(request,response,s);
            }else if(request.getParameter("kCollezionista")!=null){
                Collezionista co = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(Integer.parseInt(request.getParameter("kCollezionista")));
                delete_condivisione(request,response,s,co);
            }
            else{
                 dataM.put("collezioniList",collezioniCondivise);
            dataM.put("numero",0);
            
            t.process(dataM, response.getWriter());
            }
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaCollezioniCondivise.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaCollezioniCondivise.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaCollezioniCondivise.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaCollezioniCondivise.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ServletDiProvaCollezioniCondivise.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
