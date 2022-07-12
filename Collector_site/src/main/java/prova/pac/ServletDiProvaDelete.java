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
       if(request.getParameter("dK")!=null){
           try {
               //cancello un disco dalla vista di una collezione
               int idColl = (int) s.getAttribute("collezioneSelezionata");
               Disco disco = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco(Integer.parseInt(request.getParameter("dK")));
               ((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().deleteDisco(disco);
               response.sendRedirect("servletDiProvaVistaCollezione?k="+idColl);
               
           } catch (DataException ex) {
               Logger.getLogger(ServletDiProvaDelete.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(ServletDiProvaDelete.class.getName()).log(Level.SEVERE, null, ex);
           }
       }else{
           try {
               int idColl = Integer.parseInt(request.getParameter("cK"));
               Collezione c = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneById(idColl);
               ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().deleteCollezione(c);
               response.sendRedirect("servletDiProvaHome");
           } catch (DataException ex) {
               Logger.getLogger(ServletDiProvaDelete.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(ServletDiProvaDelete.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
          
       
    }

       
}
