package model;
import java.util.Date;


public class Entreprise {
	private int IdEts;
	private String NomEts; 
	private String LocalisationEts;
	
	public Entreprise( int IdEts, String NomEts,  String LocalisationEts ) {
		this.IdEts =IdEts;
		this.NomEts = NomEts;
		this.LocalisationEts =LocalisationEts;
	}
	
	public int getId_Ets() {
		return IdEts;
	}
	public void setId_Ets(int IdEts) {
		this.IdEts= IdEts;
	}
	
	
	public String getNom_Ets() {
		return NomEts;
	}
	
	public void setNom_Ets(String NomEts) {
		this.NomEts = NomEts;
	}
	
	
	public String getLocal_Ets() {
		return LocalisationEts;
	}
	
	public void setLocal_Ets(String LocalisationEts) {
		this.LocalisationEts = LocalisationEts;
	}
	
	
	
}

