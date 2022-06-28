/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package collector_site.framework.result;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fabri
 */
public interface DataModelFiller {

    //request e context POSSONO ESSERE NULL
    //request and context MAY BE NULL
    public void fillDataModel(Map datamodel, HttpServletRequest request, ServletContext context);
}
