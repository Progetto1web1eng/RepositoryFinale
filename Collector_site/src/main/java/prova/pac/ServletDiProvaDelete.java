/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.model.Collezione;
import collector_site.data.model.Disco;
import collector_site.framework.data.DataException;
import collector_site.framework.result.ProvaConfig;
import freemarker.template.Template;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.HashMap;
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
public class ServletDiProvaDelete extends  ServletDiProvaCollector_siteBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
       HttpSession s = request.getSession(false);
       int IDcollezionista = (int)s.getAttribute("id");
       ProvaConfig pcg = new ProvaConfig(getServletContext());
       out.println("bestia0");
       if(request.getParameter("dK")!=null){
           try {
                out.println("bestia1");
               //cancello un disco dalla vista di una collezione
               int idColl = (int) s.getAttribute("collezioneSelezionata");
                out.println("bestia2");
               Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco(Integer.parseInt(request.getParameter("dK")));
               out.println("prima della delete");
               ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().deleteDisco(disco);
               out.println("dopo la delete");
               response.sendRedirect("servletDiProvaVistaCollezione?k="+idColl);
           } catch (DataException ex) {
               Logger.getLogger(ServletDiProvaDelete.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(ServletDiProvaDelete.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
          
       
    }

       
}
