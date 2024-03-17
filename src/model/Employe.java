package model;

import java.util.Date;
import java.util.*;

public class Employe {

		private int IdEmploye;
		private String NomEmploye;
		private String PrenomEmploye;
		private float SalaireEmploye;
		private int AgeEmploye;
		private String TelEmploye;
		private int idEts;
		
		public Employe(int IdEmploye, String NomEmploye, String PrenomEmploye, float SalaireEmploye, String TelEmploye, int idEts, int AgeEmploye  ) {
			this.IdEmploye = IdEmploye;
			this.NomEmploye = NomEmploye;
			this.PrenomEmploye = PrenomEmploye;
			this.SalaireEmploye = SalaireEmploye;
			this.AgeEmploye = AgeEmploye;
			this.TelEmploye = TelEmploye;
			this.idEts = idEts;
		}
		
		public int getId_Emp() {
			return IdEmploye;
		}
		
		public void setId_Emp(int IdEmploye) {
			this.IdEmploye = IdEmploye;
		}
		
		
		public String getNom_Employe() {
			return NomEmploye; 
		}
		
		public void setNom_Employe(String NomEmploye) {
		    this.NomEmploye = NomEmploye;
		}
		
		
		public String getPrenom_Employe() {
			return PrenomEmploye;
		}
		
		public void setPrenom_Employe(String PrenomEmploye) {
			this.PrenomEmploye = PrenomEmploye;
		}
		
		
		public float getSalaire_Employe() {
			return SalaireEmploye;
		}
		
		public void setSalaire_Employe(float SalaireEmploye) {
				this.SalaireEmploye = SalaireEmploye;
		}
		

		public int getAge_Employe() {
			return IdEmploye;
		}
		
		public void setAge_Employe(int AgeEmploye) {
			this.AgeEmploye = AgeEmploye;
		}
		
		
		public String getTel_Employe() {
			return TelEmploye; 
		}
		
		public void setTel_Employe(String TelEmploye) {
		    this.TelEmploye = TelEmploye;
		}
		
		
		public int getId_Ets() {
			return idEts;
		}
		
		public void setId_Ets(int idEts) {
			this.idEts = idEts;
		}
		
		
		
}

