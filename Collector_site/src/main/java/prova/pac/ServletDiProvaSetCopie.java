/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.CopieStato;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.framework.data.DataException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
public class ServletDiProvaSetCopie extends ServletDiProvaCollector_siteBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            HttpSession s = request.getSession(false);
            int idColl = (int) s.getAttribute("collezioneSelezionata");
            int idDisc = Integer.parseInt(request.getParameter("dK"));
            List<CopieStato> cs = new ArrayList();
            Disco d = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco(idDisc);
            Collezionista collezionista = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById((int) s.getAttribute("id"));
            if(request.getParameter("st").equals("NUOVO")){
                // caso in cui stiamo chiamando l'incremento/decremento allo stato nuovo
                if(Integer.parseInt(request.getParameter("incr"))==1){
                    
                    // vogliamo incrementare
                    cs = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, collezionista);
                    int temp = cs.get(0).getNumCopieDisco();
                    temp++;
                    cs.get(0).setNumCopieDisco(temp);
                    //qui devo fare l'update delle copie stato
                    response.sendRedirect("servletDiProvaVistaCollezione?k="+idColl); 
               
                }else{
                    // vogliamo decrementare
                    cs = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, collezionista);
                    int temp = cs.get(0).getNumCopieDisco();
                    temp--;
                    cs.get(0).setNumCopieDisco(temp);
                    //qui devo fare l'update delle copie stato
                    response.sendRedirect("servletDiProvaVistaCollezione?k="+idColl); 
                    
                }
            }else{
                // caso in cui stiamo chiamando l'incremento/decremento allo stato usato
                if(Integer.parseInt(request.getParameter("incr"))==1){
                    // vogliamo incrementare
                    cs = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, collezionista);
                    int temp = cs.get(1).getNumCopieDisco();
                    temp++;
                    cs.get(1).setNumCopieDisco(temp);
                    //qui devo fare l'update delle copie stato
                    response.sendRedirect("servletDiProvaVistaCollezione?k="+idColl); 
                    
                    
                }else{
                    // vogliamo decrementare
                     cs = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getCopieStati(d, collezionista);
                    int temp = cs.get(1).getNumCopieDisco();
                    temp--;
                    cs.get(1).setNumCopieDisco(temp);
                    //qui devo fare l'update delle copie stato
                    response.sendRedirect("servletDiProvaVistaCollezione?k="+idColl); 
                    
                }
            }
            //servletDiProvaSetCopie?st=${cs.stato.toString()}&dK=${d.key}&Incr=1
            // la redirect è response.sendRedirect("servletDiProvaVistaCollezione?k="+idColl); 
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaSetCopie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaSetCopie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
