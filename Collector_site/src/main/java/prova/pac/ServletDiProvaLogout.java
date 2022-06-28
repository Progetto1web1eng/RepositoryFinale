/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import collector_site.framework.result.ProvaConfig;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.PrintWriter;
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
public class ServletDiProvaLogout extends HttpServlet {

   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s=request.getSession(false);
        s.invalidate();
        ProvaConfig pcg = new ProvaConfig(getServletContext());
        Template t = pcg.getTemplate("dispatcherUnlogged.ftl.html");
        try {
            t.process(null,response.getWriter());
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaLogout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
