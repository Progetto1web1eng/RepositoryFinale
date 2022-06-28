/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.framework.result;

import collector_site.framework.security.SecurityHelpers;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import java.util.List;

/**
 *
 * @author fabri
 */
public class SplitSlashesFmkExt implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        //la lista contiene i parametri passati alla funzione nel template
        //the list contains the parameters passed to the function in the template
        if (!list.isEmpty()) {
            return SecurityHelpers.stripSlashes(list.get(0).toString());
        } else {
            //e' possibile ritornare qualsiasi tipo che sia gestibile da Freemarker (scalari, hash, liste)
            //it is possible tor eturn any data type recognized by Freemarker (scalars, hashes, lists)
            return "";
        }
    }
}

