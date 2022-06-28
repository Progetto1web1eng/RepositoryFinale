/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package prova.pac;

import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fabri
 */
public class ServletDiProvaCollezioniCondivise extends HttpServlet {

   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Configuration cfg= new Configuration(Configuration.VERSION_2_3_0);
        cfg.setDefaultEncoding("utf-8");
        cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
        cfg.setServletContextForTemplateLoading(getServletContext(),"template");
        DefaultObjectWrapperBuilder ob = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_0);
        cfg.setObjectWrapper(ob.build());
        Template t = cfg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
            dataM.put("numero",0);
        try {
            t.process(dataM, response.getWriter());
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaCollezioniCondivise.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }

}
