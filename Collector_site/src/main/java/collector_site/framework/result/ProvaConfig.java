/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.framework.result;

import freemarker.core.HTMLOutputFormat;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import java.io.IOException;
import javax.servlet.ServletContext;

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