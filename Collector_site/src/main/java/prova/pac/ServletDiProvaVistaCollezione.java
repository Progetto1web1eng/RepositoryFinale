/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.CopieStato;
import collector_site.data.impl.DiscoImpl;
import collector_site.data.model.Artista;
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
            int idColl = Integer.parseInt(idcollS);
            
            try {        // estraggo le collezioni dal collezionista che ha effettuato il login per la visualizzazione del side menu
                Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById((int)s.getAttribute("id"));
                List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                dataM.put("collezioni",collezioni);
                        // estraggo la lista di dischi data una collezione
                        
                Collezione collezioneSelezionata = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(idColl);
                List<Disco> dischiList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDiscoByCollezione(collezioneSelezionata);
               
                s.setAttribute("collezioneSelezionata",idColl);
                
                // trovo gli artisti per ogni disco  per poi aggiungere una lista al data model
                List<Artista> artistiList = new ArrayList();
                for(Disco d : dischiList){
                    artistiList.add(((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistaByDisco(d));
                }
                
                // trovo le copie per ogni disco per poi aggiungere una lista al data model
               
                List<List<CopieStato>> csList = new ArrayList();
                List<CopieStato> tempList = new ArrayList();
                
                 List<String> immaginiList = new ArrayList();
                 List<Immagine> tempImmList = new ArrayList();
                 
                 //per l'immagine della collezione
                 String collezioneImm = "";
                 if(dischiList==null || dischiList.size()==0){
                      collezioneImm = "defaultIMG.png";
                 }else{
                     //collezioneImm è uguale all'immagine del primo disco
                    if( ((Collector_siteDataLayer) request.getAttribute("datalayer")).getImmagineDAO().getImmaginiByDisco(dischiList.get(0)).size()==0 ){
                          collezioneImm = "defaultIMG.png";
                    }else{
                          collezioneImm = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getImmagineDAO().getImmaginiByDisco(dischiList.get(0)).get(0).getFilename();
                    } 
                 }
                 /////////////////////////////////
                 
                 
                 
                for (Disco d : dischiList){
                    tempList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, collezionista);
                    csList.add(tempList);
                    
                    tempImmList = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getImmagineDAO().getImmaginiByDisco(d);
                    if(tempImmList == null || tempImmList.size()==0){
                        immaginiList.add("defaultIMG.png");
                    }else{
                        immaginiList.add(tempImmList.get(0).getFilename());
                    }
                }
                
                
                dataM.put("collezioneImm",collezioneImm);
                dataM.put("immaginiList",immaginiList);
                dataM.put("csList",csList);
                dataM.put("artistiList", artistiList);
                dataM.put("collezioneSelezionata",collezioneSelezionata);
                dataM.put("dischiList",dischiList);

            
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
