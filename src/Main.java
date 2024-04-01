import ressources.mysql.mysqlDAO;
import ressources.postgressql.postgressqlDAO;
import ressources.csv.CSVDAO;
import ressources.xml.xmlDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Connexion à MySQL
            mysqlDAO mysqlDao = new mysqlDAO();
            mysqlDao.connect("jdbc:mysql://localhost:3306/bdfrance1", "root", "");

            // Connexion à PostgreSQL
            Connection postgresConnection = postgressqlDAO.getConnection();
            postgressqlDAO postgressqlDao = new postgressqlDAO(postgresConnection);

            // CSV
            String salairesFilePath = "C:\\Users\\DELL\\Desktop\\csv\\salaires.csv";
            String congesFilePath = "C:\\Users\\DELL\\Desktop\\csv\\conges.csv";
            String bonusesFilePath = "C:\\Users\\DELL\\Desktop\\csv\\bonuses.csv";
            CSVDAO csvDAO = new CSVDAO(salairesFilePath, congesFilePath, bonusesFilePath);
            
            
            // XML
            xmlDAO xmlDao = new xmlDAO("C:\\Users\\DELL\\Desktop\\xml\\bdChina.xml");
            
            
            // Récupération des données

            // Récupération des salaires moyens par entreprise depuis MySQL
            Map<String, BigDecimal> averageSalaryByCompany = mysqlDao.getAverageSalaryByCompany();

            // Récupération des salaires moyens par entreprise depuis PostgreSQL
            Map<String, BigDecimal> averageSalaryByCompanyPostgres = postgressqlDao.salaireMoyenParentreprise();

            // Récupération des jours de congé par employé depuis MySQL
            Map<String, Integer> totalDaysOffByEmployee = mysqlDao.getTotalDaysOffByEmployee();

            // Récupération des jours de congé par employé depuis PostgreSQL
            Map<Integer, String> totalDaysOffByEmployeePostgres = new HashMap<>();
            postgressqlDao.nombreTotalJoursCongeParEmploye(postgresConnection);
            
            Map<String, BigDecimal> EmployeesWithTotalBonus = mysqlDao.getEmployeesWithTotalBonus();
            Map<String, BigDecimal> EmployeesWithTotalBonus2 = postgressqlDao.getEmployeesWithTotalBonus2();

            
			Map<String, Double> AvergeLeaveDurationByEmployee = mysqlDao.getAverageLeaveDurationByEmployee();
			
			Map<String, String> afficherEmployeesAvecCongesEtBonus = new HashMap<>();
			postgressqlDao.afficherEmployesAvecCongesEtBonus(postgresConnection);
			
			
			 Map<Integer, Double> pourcentagesSalaire = postgressqlDao.pourcentageSalaireParRapportMoyenneentreprise();
			 
			
		      Map<Integer, Double> avgLeaveDurationPOST = postgressqlDao.calculateAverageLeaveDuration(postgresConnection);
		      
		   Map<String, Double[]> employeesBonusSalary = postgressqlDao.getEmployeesBonusSalary(postgresConnection);
			
          Map<Date, Double> montantTotalBonusParMois = postgressqlDao.getMontantTotalBonusParMois(postgresConnection);
          



	           String dateDebutConges = "2024-05-20";
	           String dateFinConges = "2024-05-25";
            
            
            
            
            //1-:::::::::::Dépenses totales de salaire pour l'ets:::::::::::::::::::::::::::::::::::::::
            //CSV   
	           // Appeler la méthode calculerDepensesSalairesEntreprise() sur l'instance créée
	           double depensesTotalesSalaires = csvDAO.calculerDepensesSalairesEntreprise();

	           // Afficher le résultat
	           System.out.println("Dépenses totales de salaires : Allemagne" + depensesTotalesSalaires);
	           
            //MYSQL 
	           try {
	               // Appeler la méthode getTotalSalaryExpenses() sur l'instance créée
	               BigDecimal totalExpenses = mysqlDao.getTotalSalaryExpenses();

	               // Afficher le résultat
	               System.out.println("Dépenses totales de salaires pour l'entreprise : FRANCE " + totalExpenses);
	           } catch (SQLException e) {
	               e.printStackTrace();
	           }
	         
	         
            
            // postgres 
	           try {
	               // Appeler la méthode calculerDepensesSalaires() sur l'instance créée
	               BigDecimal depensesTotales = postgressqlDao.calculerDepensesSalaires();

	               // Afficher le résultat
	               System.out.println("Dépenses totales de salaires pour l'entreprise : USA " + depensesTotales);
	           } catch (SQLException e) {
	               e.printStackTrace();
	           }
	           
            
            //XML 
	           xmlDao.calculerDepensesSalairesEntreprise();
			
			
			
			
			
			
			
			
			
            
            
            //2::::::::::Liste des employés avec leur salaire moyen par ets
            
            //CSV
            Map<String, Double> salaireMoyenParEntreprise1 = csvDAO.calculerSalaireMoyenParEntreprise();

            // Affichage des résultats CSV
            System.out.println("Salaire moyen par entreprise (Allemagne):");
            for (Map.Entry<String, Double> entry : salaireMoyenParEntreprise1.entrySet()) {
                String companyName = entry.getKey();
                Double averageSalary = entry.getValue();
                System.out.println(companyName + ": " + averageSalary);
            }
            
            //MySQL
            
            // Affichage des salaires moyens par entreprise depuis MySQL
            System.out.println("Liste des ets avec leur salaire moyen d'employé_FRANCE:");
            for (Map.Entry<String, BigDecimal> entry : averageSalaryByCompany.entrySet()) {
                String companyName = entry.getKey();
                BigDecimal averageSalary = entry.getValue();
                System.out.println(companyName + ": " + averageSalary);
            }
            //Postgres
            
            // Affichage des salaires moyens par entreprise depuis PostgreSQL
            System.out.println("Liste des ets avec leur salaire moyen d'employé_USA:");
            for (Map.Entry<String, BigDecimal> entry : averageSalaryByCompanyPostgres.entrySet()) {
                String companyName = entry.getKey();
                BigDecimal averageSalary = entry.getValue();
                System.out.println(companyName + ": " + averageSalary);
            }
            
            //XML 
            xmlDao.afficherSalaireMoyenParEntreprise();
            
            
            
            
            
            
            
            
            
            
            
            
            //3:::::::::::::::::::::Nbr total de jours de congés par employé
            
            
            //CSV 
            System.out.println("Nbr Total congé par employé (Allemagne):");
            // Affichage des jours de congé par employé depuis le fichier CSV
               Map<Integer, Integer> totalDaysOffByEmployeeCSV = csvDAO.calculerTotalJoursCongeParEmploye();
               for (Map.Entry<Integer, Integer> entry : totalDaysOffByEmployeeCSV.entrySet()) {
                   Integer idEmploye = entry.getKey();
                   Integer nombreTotalJoursConge = entry.getValue();
                   System.out.println("ID Employé : " + idEmploye + ", Nombre total de jours de congé : " + nombreTotalJoursConge);
               }
               
               //MySQL
               // Affichage des jours de congé par employé depuis MySQL
               System.out.println("Nombre total de jours de congé par employé_FRANCE:");
               for (Map.Entry<String, Integer> entry : totalDaysOffByEmployee.entrySet()) {
                   String employeeName = entry.getKey();
                   int daysOff = entry.getValue();
                   System.out.println(employeeName + ": " + daysOff + " jours de congé");
               }
               
               //Postgres
               for (Map.Entry<Integer, String> entry : totalDaysOffByEmployeePostgres.entrySet()) {
                   Integer idEmploye = entry.getKey();
                   String nom = entry.getValue();
                   System.out.println("Nombre total de jours de congé par employé_USA :ID : " + idEmploye + ", Employé : " + nom);
               }
            
   
               //XML 
               
              xmlDao.calculerNombreTotalJoursCongeParEmploye();
               
               
               
               
               
               
               
               
               //4::::::::::::::: Liste des employés ayant pris des congés sans bonus
               //CSV 
              
               
               //MySQL 
               
               try {
                   // Obtenez la liste des employés ayant pris des congés mais n'ayant pas de bonus
                   List<String> employeesWithDaysOffWithoutBonus = mysqlDao.getEmployeesWithDaysOffWithoutBonus();

                   // Affichez les résultats
                   System.out.println("Liste des employés SANS BONUS :  france");
                   for (String employeeName : employeesWithDaysOffWithoutBonus) {
                       System.out.println(employeeName);
                   }
               } catch (SQLException e) {
                   // Gérez les exceptions SQL
                   e.printStackTrace();
               }
              
               //POTGRESSQL  
               try {
                   // Obtenez la liste des employés sans bonus
                   List<Integer> employesSansBonus = postgressqlDao.employesSansBonus();

                   // Affichez les résultats
                   System.out.println("Liste des employés sans bonus : usa");
                   for (Integer idEmploye : employesSansBonus) {
                       System.out.println(idEmploye);
                   }
               } catch (SQLException e) {
                   // Gérez les exceptions SQL
                   e.printStackTrace();
               }
               
               
               //XML 
               xmlDao.afficherEmployesCongesSansBonus();
               
               
               
               
               
               
               
               
               
               
               
              //5 - ::::::::::::::::Liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus
               //csv 
               System.out.println("Liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus : Allemagne");
               csvDAO.listeEmployesAvecCongesEtBonus();
               
               
               //MySQL
               System.out.println("Liste des employés congé et Total Bonus FRANCE");
   			for(Entry<String, BigDecimal> entry : EmployeesWithTotalBonus.entrySet())  {
               	String employename=entry.getKey();
                   BigDecimal totalBonus=entry.getValue();
                   System.out.println(employename + ": " +totalBonus);
                }
   			
   			//POSTGRES
   			
   			System.out.println("Liste des employés congés et le total des bonus : USA");
   			for(Entry<String, BigDecimal> entry : EmployeesWithTotalBonus2.entrySet())  {
               	String employename=entry.getKey();
                   BigDecimal totalBonus=entry.getValue();
                   System.out.println(employename + ": " +totalBonus);
                }			

            //XML
   			
   			xmlDao.afficherEmployesCongesAvecTotalBonus();
   			
   			
   			
   			
   			
   			
   			
   			
   			
   			
   			
   			
   			
   			//6:::::::::::::::::::::Calcul de la durée moyenne de congé 
   			
   			//CSV 
   	        Map<Integer, Double> dureeMoyenneCongeParEmploye = csvDAO.calculerDureeMoyenneCongeParEmploye();
   	        System.out.println("Durée moyenne de congé par employé (en jours) :");
   	        for (Map.Entry<Integer, Double> entry : dureeMoyenneCongeParEmploye.entrySet()) {
   	            System.out.println("ALLEMAGNE duree moy cong Employé avec ID " + entry.getKey() + " : " + entry.getValue() + " jours");
   	        } 
   			
   			
            //MYSQL
			System.out.println("Calcul de la durée moyenne de congé par employé, avec un classement : FRANCE");
			for(Map.Entry<String,Double> entry : AvergeLeaveDurationByEmployee.entrySet())  {
				String employename=entry.getKey();
			    double avgDuration =entry.getValue();
			    System.out.println(employename + ": " +avgDuration);
			 }
			
			//POSTGRES
			
			System.out.println("USA Calcul de la durée moyenne de congé par employé, avec un classement : USA");
			
			for (Map.Entry<Integer, Double> entry : avgLeaveDurationPOST.entrySet()) {
	                int idEmploye = entry.getKey();
	                double averageLeaveDuration = entry.getValue();
	                System.out.println("Employé avec ID " + idEmploye + " : Moyenne de congé = " + averageLeaveDuration + " jours");
	         }
            //xml 
			xmlDao.calculerDureeMoyenneCongeParEmploye();
			
			
			

           
            
            
			
			
			//7:::::::::::::::::::::::::::Liste des employéés ayant pris des congés et des bonus dans la meme periode 
			//CSV 
			System.out.println("Allemagne Liste des employés qui ont pris des congés et des bonus dans la même période :");
	        csvDAO.listeEmployesAvecCongesEtBonusMemePeriode();
			
			
			//mysql
			
	           try {
	                List<String> employeesBonusCongeMeme = mysqlDao.getEmployeesWithSamePeriod();

	                // Affichez les résultats
	                System.out.println("Listedes employés qui ont pris des congéset des bonus dans la meme periode FRANCE :");
	                for (String employeeName : employeesBonusCongeMeme) {
	                    System.out.println(employeeName);
	                }
	            } catch (SQLException e) {
	                // Gérez les exceptions SQL
	                e.printStackTrace(); 
	            }
          
            //Postgres
	           
	           
	           
	           // Obtenez la liste des employés sans bonus
			   List<String> employeesWithSamePeriod = postgressqlDao.getEmployeesWithSamePeriod(postgresConnection);

			   // Affichez les résultats
			   System.out.println("Liste des employés conge et  bonus meme temps _usa");
			   for (String employee : employeesWithSamePeriod) {
			       System.out.println(employee);
			   }
	           


	        //xml 
	           xmlDao.afficherEmployesCongesEtBonusMemePeriode();
	            
	            
	            
	            
	            
	            
	            
	            
	            
	            
	            
	            
	            //8::::::::::::::::::::::::::::::::::Liste des employés montant  bonus moyenne salaire congé date spécifique::::::::::::
	            
	            //csv
	            
	            
	            //mysql
	           Map<Integer, Map<String, Object>> employesDetails = mysqlDAO.getEmployesDetails(mysqlDao.getConnection(), dateDebutConges, dateFinConges);
	            
	            // Affichage des détails des employés
	            for (Map.Entry<Integer, Map<String, Object>> entry : employesDetails.entrySet()) {
	                Integer idEmploye = entry.getKey();
	                Map<String, Object> details = entry.getValue();
	                System.out.println("FRANCE PLAGE DATE ID Employé: " + idEmploye);
	                System.out.println("Détails: " + details);
	            }
	            
	   		    //postgres 
	            System.out.println("PLAGE DATE_Liste des employés avec le montant total des bonus et la moyenne des salaires :");
		           for (Map.Entry<String, Double[]> entry : employeesBonusSalary.entrySet()) {
		               String employeeName = entry.getKey();
		               Double[] values = entry.getValue();
		               System.out.println("Nom de l'employé : " + employeeName + ", Montant total des bonus : " + values[0] + ", Moyenne des salaires : " + values[1]);
		           }
		           //xml
		           String dateDebutPlage = "2024-01-01";
		           String dateFinPlage = "2024-12-31";
		           
		           xmlDao.afficherEmployesAvecBonusEtMoyenneSalaireDansPlageDates(dateDebutPlage, dateFinPlage);

            
	           	
	           //9::::::::::::::::::::::Calcul total bonus distrubués chaque mois année précedente
	           
	           //csv

		           Map<String, Double> totalBonusParMoisAnneePrecedente = csvDAO.calculerTotalBonusParMoisAnneePrecedente();
		           System.out.println("Montant total des bonus distribués chaque mois de l'année précédente _Allemagne");
		           for (Map.Entry<String, Double> entry : totalBonusParMoisAnneePrecedente.entrySet()) {
		               System.out.println(entry.getKey() + " : " + entry.getValue());
		           }
		           
		           
		           //mysql
		       
		           Map<String, Double> montantTotalBonusParMois2 = mysqlDAO.calculerMontantTotalBonusParMoisMysql(mysqlDao.getConnection());

		            // Utilisation du résultat obtenu
		            for (Map.Entry<String, Double> entry : montantTotalBonusParMois2.entrySet()) {
		                String mois = entry.getKey();
		                double montantTotalBonus = entry.getValue();
		                System.out.println("France_ Mois : " + mois + ", Montant total bonus : " + montantTotalBonus);
		            }
				
		           
		       
		            
	           
	           // postgres
		           System.out.println("USA2 _ Montant total des bonus distribués chaque mois au cours de l'année précédente :");
		           for (Map.Entry<Date, Double> entry : montantTotalBonusParMois.entrySet()) {
		               Date mois = entry.getKey();
		               Double montantTotalBonus = entry.getValue();
		               System.out.println("Mois : " + mois + ", Montant total des bonus : " + montantTotalBonus);
		           } 
	           	           
	           
	           //xml
		           xmlDao.calculerMontantTotalBonusParMoisAnneePrecedente();

	           
	           
	           //10:::::::::::::::liste des employés avec pourcentage du salaire par rapport a la moyenne de l'ets
	           
	           
	           //csv
		           System.out.println("Liste des employés avec le pourcentage du salaire par rapport à la moyenne de leur entreprise :");
		           csvDAO.listeEmployesAvecPourcentageSalaireParRapportMoyenneEntreprise();
		           
	           //mysql
		           Map<Integer, Double> pourcentageSalaireParEmploye = mysqlDAO.calculerPourcentageSalaireParEmploye(mysqlDao.getConnection());
		            
		            // Utilisation du résultat
		            for (Map.Entry<Integer, Double> entry : pourcentageSalaireParEmploye.entrySet()) {
		                System.out.println("France_pourcentage_Employé ID: " + entry.getKey() + ", Pourcentage Salaire: " + entry.getValue());
		            }
	           
	           
	           
	           //Postgres 
	        // Affichage des pourcentages de salaire par rapport à la moyenne de l'entreprise
	            System.out.println("Pourcentage de salaire par rapport à la moyenne de l'entreprise :");
	           
				for (Map.Entry<Integer, Double> entry : pourcentagesSalaire.entrySet()) {
	                Integer idEmploye = entry.getKey();
	                Double pourcentage = entry.getValue();
	                System.out.println("ID Employé : " + idEmploye + ", Pourcentage : " + pourcentage);
	            }
	            
	           //xml 
				
				
				
				

			
			
            
            // Fermeture des connexions
            mysqlDao.closeConnection();
            postgresConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}














