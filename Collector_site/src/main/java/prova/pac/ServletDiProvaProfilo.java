/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.Genere;
import collector_site.data.model.Artista;
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
public class ServletDiProvaProfilo extends ServletDiProvaCollector_siteBaseController {

  
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            HttpSession s = request.getSession(false);
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
            
            Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById((int)s.getAttribute("id"));
            List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
            dataM.put("collezioni",collezioni);
            
            // cerco gli artisti preferiti
            List<Artista> artistiPrefList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistiPreferiti(collezionista);
            if(artistiPrefList.size()==0){
                dataM.put("hidden", 3);
                //non ci sono artisti preferiti
            }else if(artistiPrefList.size()==1){
                 dataM.put("hidden", 2);
                 dataM.put ("artistaPref1",artistiPrefList.get(0));
                //hai 1 artista preferito
            }
            else if(artistiPrefList.size()==2){
                 dataM.put("hidden", 1);
                  dataM.put ("artistaPref1",artistiPrefList.get(0));
                   dataM.put ("artistaPref2",artistiPrefList.get(1));
                // hai due artisti preferiti
            }
            else{
                 dataM.put("hidden", 0);
                  dataM.put ("artistaPref1",artistiPrefList.get(0));
                   dataM.put ("artistaPref2",artistiPrefList.get(1));
                    dataM.put ("artistaPref3",artistiPrefList.get(2));
                //hai 3 artisti preferiti
            }
            
            
            List<Genere> generiPrefList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getGeneriPreferiti(collezionista);
            if( generiPrefList.size()==0){
                dataM.put("h", 3);
                //non ci sono artisti preferiti
            }else if( generiPrefList.size()==1){
                 dataM.put("h", 2);
                 dataM.put ("gPref1", generiPrefList.get(0).toString());
                //hai 1 artista preferito
            }
            else if( generiPrefList.size()==2){
                 dataM.put("h", 1);
                  dataM.put ("gPref1",generiPrefList.get(0).toString());
                   dataM.put ("gPref2",generiPrefList.get(1).toString());
                // hai due artisti preferiti
            }
            else{
                 dataM.put("h", 0);
                  dataM.put ("gPref1",generiPrefList.get(0).toString());
                   dataM.put ("gPref2",generiPrefList.get(1).toString());
                    dataM.put ("gPref3",generiPrefList.get(2).toString());
                //hai 3 artisti preferiti
            }
            

            dataM.put("numero",5);
            dataM.put("collezionista", collezionista);
            t.process(dataM,  response.getWriter());
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaProfilo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaProfilo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaProfilo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaProfilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
