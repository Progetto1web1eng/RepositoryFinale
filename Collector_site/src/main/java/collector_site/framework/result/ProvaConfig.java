/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.framework.result;

import freemarker.core.HTMLOutputFormat;
import freemarker.core.JSONOutputFormat;
import freemarker.core.ParseException;
import freemarker.core.XMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fabri
 
 
 BISOGNA SETTARE TUTTE LE COSE SUL WEB.XML QUESTA è ANCORA LA VERSIONE DEL PROF !!!!
 
 
 
 
 
 
 */
public class ProvaConfig {

    protected ServletContext context;
    protected Configuration cfg;
    
    public ProvaConfig(ServletContext context) {
        this.context = context;
        init();
    }
    // per creare un oggetto scrivere:
    //ProvaConfig pcg = new ProvaConfig(getServletContext());

    private void init() {
       
        cfg = new Configuration(Configuration.VERSION_2_3_26);
        cfg.setOutputEncoding("utf-8");
        cfg.setDefaultEncoding("utf-8");
        cfg.setServletContextForTemplateLoading(context,"template");
        cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
        DefaultObjectWrapperBuilder ob = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_0);
        cfg.setObjectWrapper(ob.build());

    }
    public Template getTemplate(String s) throws MalformedTemplateNameException, ParseException, IOException{
        return cfg.getTemplate(s);
    }



}