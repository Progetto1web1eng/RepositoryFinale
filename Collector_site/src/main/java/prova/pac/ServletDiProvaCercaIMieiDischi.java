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
public class ServletDiProvaCercaIMieiDischi extends ServletDiProvaCollector_siteBaseController {
   
    private void cerca_InputAction(HttpServletRequest request, HttpServletResponse response,Map<String,Object> dataM, int IDcollezionista){
        try {
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            //completo la sideBar con la lista di collezioni
            Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
            List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
            dataM.put("collezioni",collezioni);
            
            String inputDaCercare = request.getParameter("cercaIMieiDischi");
            if(  !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":T"))&&
                    !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":A"))&&
                    !(inputDaCercare.substring(inputDaCercare.length()-2).equals(":G"))
                    ){
                // assegno un tipo di ricerca di default nel caso in cui l'utente non abbia scelto i suggerimenti 
                inputDaCercare = inputDaCercare+":T";
                out.println("se non ci sono suggerimenti: "+inputDaCercare);
            }
                if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":T")){ //ricerca per titolo
                    
                    //prova
                    dataM.put("numero",4);
                    dataM.put("hidden",0); //non verrà visualizzato il div con la lista di dichi
                    t.process(dataM, response.getWriter());
                    out.println("normale: "+inputDaCercare);
                    
                }
                if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":A")){ //ricerca per artista
                    
                    //prova
                    dataM.put("numero",4);
                    dataM.put("hidden",0); //non verrà visualizzato il div con la lista di dichi
                    t.process(dataM, response.getWriter());
                    out.println("normale: "+inputDaCercare);
                    
                }
                if(inputDaCercare.substring(inputDaCercare.length()-2).equals(":G")){ //ricerca per genere
                   
                    //prova
                    dataM.put("numero",4);
                    dataM.put("hidden",0); //non verrà visualizzato il div con la lista di dichi
                    t.process(dataM, response.getWriter());
                    out.println("normale: "+inputDaCercare);
                    
                }
            
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        try {
            HttpSession s = request.getSession(false);
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
            if(request.getParameter("cercaIMieiDischi")==null){
                // estraggo le collezioni dal collezionista che ha effettuato il login per la visualizzazione del side menu
                Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById((int)s.getAttribute("id"));
                List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                dataM.put("collezioni",collezioni);
                dataM.put("numero",4);
                dataM.put("hidden",0); //non verrà visualizzato il div con la lista di dichi
                t.process(dataM, response.getWriter());
            }else{
                cerca_InputAction(request,response,dataM,(int)s.getAttribute("id"));
            }
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | TemplateException | DataException ex) {
            Logger.getLogger(ServletDiProvaCercaIMieiDischi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    /* vecchia
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Configuration cfg= new Configuration(Configuration.VERSION_2_3_0);
        cfg.setDefaultEncoding("utf-8");
        cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
        cfg.setServletContextForTemplateLoading(getServletContext(),"template");
        DefaultObjectWrapperBuilder ob = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_0);
        cfg.setObjectWrapper(ob.build());
        if(request.getParameter("cercaIMieiDischi")==null){
            Template t = cfg.getTemplate("dispatcherDiProva.ftl.html");
                Map<String,Object> dataM = new HashMap();
                dataM.put("numero",4);
                dataM.put("hidden",false);
            try {
                t.process(dataM, response.getWriter());
            } catch (TemplateException ex) {
                Logger.getLogger(ServletDiProvaCollezioniCondivise.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            Template t = cfg.getTemplate("dispatcherDiProva.ftl.html");
                Map<String,Object> dataM = new HashMap();
                dataM.put("numero",4);
                dataM.put("hidden",true);
            try {
                t.process(dataM, response.getWriter());
            } catch (TemplateException ex) {
                Logger.getLogger(ServletDiProvaCollezioniCondivise.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
    }*/
}

  