/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import java.io.IOException;
import java.io.PrintWriter;
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
         HttpSession s = request.getSession(false);
         int idColl = (int) s.getAttribute("collezioneSelezionata");
         int idDisc = Integer.parseInt(request.getParameter("dK"));
         if(request.getParameter("st").equals("NUOVO")){
          // caso in cui stiamo chiamando l'incremento/decremento allo stato nuovo
            if(Integer.parseInt(request.getParameter("incr"))==1){
                // vogliamo incrementare
                
            }else{
                // vogliamo decrementare
            }
         }else{
          // caso in cui stiamo chiamando l'incremento/decremento allo stato nuovo
             if(Integer.parseInt(request.getParameter("incr"))==1){
                // vogliamo incrementare
            }else{
                // vogliamo decrementare
            } 
         }
        //servletDiProvaSetCopie?st=${cs.stato.toString()}&dK=${d.key}&Incr=1
       // la redirect è response.sendRedirect("servletDiProvaVistaCollezione?k="+idColl); 
    }


}
