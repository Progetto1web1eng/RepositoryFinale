/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.data.impl;

import collector_site.framework.data.DataItemImpl;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author mauri
 */
public class CollezionistaImpl extends DataItemImpl<Integer> implements Collezionista {
        
        private String username;
	private String nickname;
	private String email;
	private String password;
	private String cellulare;
        private HashMap<Disco,CopieStato> dischiEInfo; 
        private List<Collezione> collezioniCondivise; //lista di collezioni private a cui questo collezionista ha accesso
        private List<Collezione> collezioni;
        
        public CollezionistaImpl() {
            username = "";
            nickname = "";
            email = "";
            password = "";
            cellulare = "";
            
            dischiEInfo = null;
            collezioniCondivise = null;
            collezioni = null;
        }
        
        @Override
	public String getUsername() {
		return username;
	}
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	
        @Override
	public String getNickname() {
		return nickname;
	}
	@Override
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@Override
	public String getEmail() {
		return email;
	}
	@Override
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getCellulare() {
		return cellulare;
	}
	@Override
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
        @Override
        public HashMap<Disco,CopieStato> getDischiEInfo() {
		return dischiEInfo;
	}
        @Override
	public void setDischiEInfo (HashMap<Disco,CopieStato> dischiEInfo) {
		this.dischiEInfo = dischiEInfo;
	}
	@Override
	public List<Collezione> getCollezioniCondivise() {
		return collezioniCondivise;
	}
	@Override
	public void setCollezioniCondivise(List<Collezione> collezioniCondivise) {
		this.collezioniCondivise = collezioniCondivise;
	}
	@Override
	public List<Collezione> getCollezioni() {
		return collezioni;
	}
	@Override
	public void setCollezioni(List<Collezione> collezioni) {
		this.collezioni = collezioni;
	}
}
