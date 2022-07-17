/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.framework.data.DataException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author stefa
 */
public class JsonServlet extends ServletDiProvaCollector_siteBaseController {    

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        try {
            String path = getServletContext().getInitParameter("pathProgetto");
            ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getArtistiSingoliJson(path);
            ((Collector_siteDataLayer) request.getAttribute("datalayer")).getArtistaDAO().getGruppiMusicaliJson(path);
        } catch (DataException ex) {
            Logger.getLogger(JsonServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        

        
        
        
        
    }
}
