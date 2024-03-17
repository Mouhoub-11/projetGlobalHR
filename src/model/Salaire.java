package model;
import java.util.Date;

public class Salaire {

	private int IdSalaire;
	private float MontantSalaire;
	private Date DateVersementSalaire;
	private int IdEmploye;
	
	public Salaire(int IdSalaire, float MontantSalaire, Date DateVersementSalaire,int IdEmploye  ) {
		
		this.IdSalaire =IdSalaire; 
		this.MontantSalaire = MontantSalaire;
		this.DateVersementSalaire =DateVersementSalaire;
		this.IdEmploye = IdEmploye; 
		
	}
	
	public int getId_Salaire() {
		return IdSalaire;
	}
	
	public void setId_Salaire(int IdSalaire) {
		this.IdSalaire = IdSalaire;
	}
	
	
	
	public float getMontant_Salaire() {
		return MontantSalaire;
	}
	
	public void setMontant_Salaire(float MontantSalaire) {
		this.MontantSalaire = MontantSalaire;
	}
	
	public Date getDate_VersSalaire(){
		return DateVersementSalaire;
	}
	
	public void setDate_VersSalaire(Date DateVersementSalaire) {
		this.DateVersementSalaire = DateVersementSalaire;
	}
	
	
	
	public int getId_Emp() {
		return IdEmploye;
	}
	
	public void setId_Emp(int IdEmploye) {
		this.IdEmploye = IdEmploye;
	}

}

 