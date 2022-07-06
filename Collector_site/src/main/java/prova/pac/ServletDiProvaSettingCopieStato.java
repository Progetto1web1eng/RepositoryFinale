/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.CopieStato;
import collector_site.data.impl.StatoDisco;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.framework.data.DataException;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.String.valueOf;
import static java.lang.System.out;
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
public class ServletDiProvaSettingCopieStato extends ServletDiProvaCollector_siteBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            
            HttpSession s = request.getSession(false);
           
            Disco disco = (Disco) s.getAttribute("discoSessione");
            
            int IDCollezioneSessione = Integer.parseInt((String) s.getAttribute("IDCollezioneSessione"));
            
            Collezionista collezionista = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById((int) s.getAttribute("id"));
            
            String[] parStato=request.getParameterValues("sCSSPar");
            int parCopie=Integer.parseInt(request.getParameter("sCSSI1Par"));
            
            out.println("1");
            CopieStato cp = new CopieStato();
             out.println("2");
            cp.setStato(StatoDisco.valueOf(parStato[0]));
             out.println("3");
            cp.setNumCopieDisco(parCopie);
             out.println("4");
            CopieStato np = new CopieStato();
             out.println("5");
            List<CopieStato> listcp = new ArrayList();
             out.println("6");
            if(cp.getStato().toString().equals("NUOVO")){
                np.setStato(StatoDisco.USATO);
            }else{
                np.setStato(StatoDisco.NUOVO);
            }
            np.setNumCopieDisco(0);
            listcp.add(cp);
            listcp.add(np);
            disco.setCopieStati(listcp);
             out.println("settingcopieStato3");
            ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().storeDisco(disco);
            
            ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().addDiscoToCollezionista(disco, collezionista);
            out.println("po1");
            
            ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().addDiscoToCollezione(disco,
                    ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(
                            IDCollezioneSessione));
            
            
            
            s.removeAttribute("gruppoSessione");
            s.removeAttribute("ListaArtisti");
            s.removeAttribute("discoSessione");
            s.removeAttribute("IDCollezioneSessione");
            s.removeAttribute("copieStato");
            
            response.sendRedirect("servletDiProvaVistaCollezione?k="+IDCollezioneSessione);
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaSettingCopieStato.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaSettingCopieStato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
}
