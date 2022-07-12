/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.framework.utils;

import collector_site.data.model.Disco;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fabri
 */
public class Service {
    public static List<Disco> deleteSame(List<Disco> l){
        List<Disco> returnL = new ArrayList();
        for(Disco d : l){
            if(returnL.contains(d)){
                ;
            }else{
                returnL.add(d);
            }
        }
        
        return returnL;
        
    }
}
