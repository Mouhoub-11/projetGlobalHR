package model;

import java.util.Date;


public class Conge {
	private int IdConge;
	private Date DateDebConge;
	private Date DateFinConge;
	private String RaisonConge;
	
	
	public Conge(int IdConge,  Date DateDebConge, Date DateFinConge, String RaisonConge ) {
		this.IdConge = IdConge; 
		this.DateDebConge = DateDebConge;
		this.DateFinConge = DateFinConge;
		this.RaisonConge = RaisonConge;
	}
	
	public int getId_Conge() {
		return IdConge;
	}
	
	public void setId_Conge(int IdConge) {
		this.IdConge = IdConge;
	}
	
	public Date getDate_DebConge(){
		return DateDebConge;
	}
	
	public void setDate_DebConge(Date DateDebConge) {
		this.DateDebConge = DateDebConge;
	}
	
	public Date getDate_FinConge(){
		return DateFinConge;
	}
	
	public void setDate_FinConge(Date DateFinConge) {
		this.DateFinConge = DateFinConge;
	}
	
	public String getRaison_Conge() {
		return RaisonConge;
	}
	
	public void setRaison_Conge(String RaisonConge) {
		this.RaisonConge = RaisonConge;
	}
	
	
}


