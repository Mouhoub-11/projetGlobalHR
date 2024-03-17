package model;

import java.util.Date;



public class Bonus {
	
	private int IdBonus;
	private float MontantBonus;
	private String RaisonBonus;
	private Date DateBonus;
	
	
	public Bonus(int IdBonus, float MontantBonus, String RaisonBonus, Date DateBonus) {
		this.IdBonus = IdBonus; 
		this.MontantBonus = MontantBonus; 
		this.RaisonBonus = RaisonBonus;
		this.DateBonus =DateBonus;
		
		
	}
	
	
	
	public int getId_Bonus() {
		return IdBonus;
	}
	
	public void setId_Bonus(int IdBonus) {
		this.IdBonus = IdBonus;
	}
	
	
	public float getMontant_Bonus() {
		return MontantBonus;
	}
	
	public void setMontant_Bonus(float MontantBonus) {
		this.MontantBonus = MontantBonus;
	}
	
	public String getRaison_Bonus() {
		return RaisonBonus;
	}
	
	public void setRaison_Bonus(String RaisonBonus) {
		this.RaisonBonus = RaisonBonus;
	}
	
	public Date getDate_Bonus(){
		return DateBonus;
	}
	
	public void setDate_Bonus(Date DateBonus) {
		this.DateBonus = DateBonus;
	}
	
	
	
}
