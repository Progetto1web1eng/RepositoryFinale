/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.TracciaImpl;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.model.Traccia;
import collector_site.framework.data.DataException;
import collector_site.framework.result.ProvaConfig;
import freemarker.core.ParseException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author fabri
 */
public class ServletDiProvaModificaDisco extends ServletDiProvaCollector_siteBaseController {

    private void add_T(HttpServletRequest request,HttpServletResponse response,int IDdisco,Template t,Map<String,Object> dataM,int IDcollezionista,HttpSession s) throws DataException, IOException, java.text.ParseException{
        
        String nomeTracciaPar = request.getParameter("nomeTracciaPar");
        String timeParS = request.getParameter("timePar");
        if(nomeTracciaPar.length()==0||timeParS.length()==0){
            try {
                Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
                List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                dataM.put("collezioni",collezioni);
               
                dataM.put("numero", 11);
                dataM.put("error",1);
                t.process(dataM, response.getWriter());
            } catch (TemplateException ex) {
                Logger.getLogger(ServletDiProvaModificaDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
           SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
           long ms = sdf.parse(timeParS).getTime();
           Time timePar = new Time(ms);
           Traccia traccia = new TracciaImpl();
           traccia.setDurata(timePar);
           traccia.setTitolo(nomeTracciaPar);
           Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco(IDdisco);
           traccia.setDisco(disco);
           ((Collector_siteDataLayer) request.getAttribute("datalayer")).getTracciaDAO().storeTraccia(traccia);
           response.sendRedirect("servletDiProvaModificaDisco?discoKey="+IDdisco);
        }
         
           
    }
    
    
    private void update_disco(HttpServletRequest request,HttpServletResponse response,int IDdisco,int collK) throws DataException, IOException{
        Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco(IDdisco);
        disco.setNomeDisco(request.getParameter("nomeDiscoPar"));
        if(request.getParameter("dataPar").length()!=0){
            disco.setAnno(Integer.parseInt(request.getParameter("dataPar")));
        }
        disco.setBarcode(request.getParameter("barcodePar"));
        disco.setEtichetta(request.getParameter("etichettaPar"));
        ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().storeDisco(disco);
        response.sendRedirect("servletDiProvaVistaCollezione?k="+collK);
    }
    private void schermata_traccia(HttpServletRequest request,HttpServletResponse response,Template t,Map<String,Object> dataM,int IDcollezionista,HttpSession s) throws IOException, TemplateException, DataException{
        
        Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
        List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
        dataM.put("collezioni",collezioni);
                
        s.setAttribute("idDiscoS", Integer.parseInt(request.getParameter("aggiungiTraccia")));
        dataM.put("numero", 11);
        t.process(dataM, response.getWriter());
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response){
        try {
            HttpSession s = request.getSession(false);
            int IDcollezionista = (int)s.getAttribute("id");
            ProvaConfig pcg = new ProvaConfig(getServletContext());
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
            if(request.getParameter("discoKey")!=null){
                //significa che ho cliccato il tasto modifica
                int IDdisco = Integer.parseInt(request.getParameter("discoKey"));
                s.setAttribute("discoID", IDdisco);
                if(request.getParameter("collK")!= null){
                     s.setAttribute("collK",Integer.parseInt(request.getParameter("collK")));
                }
                
                //completo la lista di collezione nella side bar
                Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById(IDcollezionista);
                List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
                dataM.put("collezioni",collezioni);
                Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco(IDdisco);
                List<Traccia> tracce = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getTracciaDAO().getTracceByDisco(disco);
                                           
                dataM.put("tracceList",tracce);
                dataM.put("disco",disco);
                dataM.put("numero", 10);
                t.process(dataM, response.getWriter());
            }else if(request.getParameter("aggiungiTraccia")!=null){
                schermata_traccia(request,response,t,dataM,IDcollezionista,s);
            }else if(request.getParameter("timePar")!=null){
                add_T(request,response, (int) s.getAttribute("idDiscoS"),t,dataM,IDcollezionista,s);
            }
            else { 
                update_disco(request,response, (int) s.getAttribute("discoID"),(int) s.getAttribute("collK"));
            }
        
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaModificaDisco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaModificaDisco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaModificaDisco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaModificaDisco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.text.ParseException ex) {
            Logger.getLogger(ServletDiProvaModificaDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
