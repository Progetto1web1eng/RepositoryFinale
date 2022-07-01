/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.impl;

import static collector_site.data.impl.StatoDisco.NUOVO;
import static collector_site.data.impl.StatoDisco.USATO;

/**
 *
 * @author fabri
 */
public class CopieStato {
    private int numCopieDisco;
    private StatoDisco stato;
    
    public CopieStato(){
        this.numCopieDisco=0;
        this.stato=NUOVO;
    }
    public CopieStato(StatoDisco stato,int numCopieDisco){
        this.numCopieDisco=numCopieDisco;
        this.stato=stato;
    }

    public void setNumCopieDisco(int numCopieDisco) {
        this.numCopieDisco = numCopieDisco;
    }

    public void setStato(StatoDisco stato) {
        this.stato = stato;
    }
    public int getNumCopieDisco(){
        return this.numCopieDisco;
    }
    public StatoDisco getStato(){
        return this.stato;
    }
}
