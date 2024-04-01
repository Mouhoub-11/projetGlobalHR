package ressources.postgressql;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.math.*;
import java.time.*;


public class postgressqlDAO {
	
    private Connection connection;

    public postgressqlDAO(Connection connection) {
        this.connection = connection;
    }
    
     

     public BigDecimal calculerDepensesSalaires() throws SQLException {
        BigDecimal depensesTotales = BigDecimal.ZERO;
        String query = "SELECT SUM(\"montantsalaire\") AS \"TotalSalaires\" FROM salaire";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                depensesTotales = resultSet.getBigDecimal("TotalSalaires");
            }
        }
        return depensesTotales;
    }

     public Map<String, BigDecimal> salaireMoyenParentreprise() throws SQLException {
        Map<String, BigDecimal> salairesMoyens = new HashMap<>();

        String query = "SELECT et.\"nomets\", AVG(s.\"montantsalaire\") AS \"SalaireMoyen\" " +
                "FROM \"employé\" e " +
                "JOIN \"entreprise\" et ON e.\"idets\" = et.\"idets\" " +
                "JOIN \"salaire\" s ON e.\"idemploye\" = s.\"idemploye\" " +
                "GROUP BY et.\"idets\"";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String entreprise = resultSet.getString("nomets");
                BigDecimal salaireMoyen = resultSet.getBigDecimal("SalaireMoyen");
                salairesMoyens.put(entreprise, salaireMoyen);
            }
        }
        return salairesMoyens;
    }



     //problem marche pas pour le moment
     public static void nombreTotalJoursCongeParEmploye(Connection connection) throws SQLException {
         PreparedStatement stmt = null;
         try {
             // Préparation de la requête SQL
             String sql = "SELECT e.IdEmploye, e.NomEmploye, e.PrenomEmploye, COUNT(c.IdConge) AS NombreTotalJoursConge " +
                          "FROM Employé e " +
                          "INNER JOIN prendre p ON e.IdEmploye = p.IdEmploye " +
                          "INNER JOIN Congés c ON p.IdConge = c.IdConge " +
                          "GROUP BY e.IdEmploye, e.NomEmploye, e.PrenomEmploye";
             stmt = connection.prepareStatement(sql);

             // Exécution de la requête
             ResultSet rs = stmt.executeQuery();

             // Affichage des résultats
             System.out.println("\nNombre total de jours de congé par employé :_USA");
             while (rs.next()) {
                 int idEmploye = rs.getInt("IdEmploye");
                 String nom = rs.getString("NomEmploye");
                 String prenom = rs.getString("PrenomEmploye");
                 int nombreTotalJoursConge = rs.getInt("NombreTotalJoursConge");
                 System.out.println("ID : " + idEmploye + ", Employé : " + nom + " " + prenom +
                                    ", Nombre total de jours de congé : " + nombreTotalJoursConge);
             }

             // Fermeture des ressources
             rs.close();
         } finally {
             // Fermeture du statement
             if (stmt != null) {
                 stmt.close();
             }
         }
     }

     
    
     public static void afficherEmployesAvecCongesEtBonus(Connection connection) throws SQLException {
         PreparedStatement stmt = null;
         try {
             // Préparation de la requête SQL
             String sql = "SELECT DISTINCT e.NomEmploye, e.PrenomEmploye " +
                          "FROM Employé e " +
                          "INNER JOIN prendre p ON e.IdEmploye = p.IdEmploye " +
                          "INNER JOIN Congés c ON p.IdConge = c.IdConge " +
                          "INNER JOIN Obtenir o ON e.IdEmploye = o.IdEmploye " +
                          "INNER JOIN Bonus b ON o.IdBonus = b.IdBonus";
             stmt = connection.prepareStatement(sql);

             // Exécution de la requête
             ResultSet rs = stmt.executeQuery();

             // Affichage des résultats
             System.out.println("Employés ayant eu des congés et des bonus :");
             while (rs.next()) {
                 String nom = rs.getString("NomEmploye");
                 String prenom = rs.getString("PrenomEmploye");
                 System.out.println(nom + " " + prenom);
             }

             // Fermeture des ressources
             rs.close();
         } finally {
             // Fermeture du statement
             if (stmt != null) {
                 stmt.close();
             }
         }
     }


    
     public List<Integer> employesSansBonus() throws SQLException {
         List<Integer> employesSansBonus = new ArrayList<>();
         String query = "SELECT p.\"idemploye\" " +
                 "FROM \"prendre\" p " +
                 "LEFT JOIN \"obtenir\" o ON p.\"idemploye\" = o.\"idemploye\" " +
                 "WHERE o.\"idemploye\" IS NULL";
         try (PreparedStatement statement = connection.prepareStatement(query)) {
             ResultSet resultSet = statement.executeQuery();
             while (resultSet.next()) {
                 int idemploye = resultSet.getInt("idemploye");
                 employesSansBonus.add(idemploye);
             }
         }
         return employesSansBonus;
     }
     
     
//    public Map<Integer, Double> dureeMoyenneCongeParEmploye() throws SQLException {
//        Map<Integer, Double> dureeMoyenneCongeParEmploye = new HashMap<>();
//        String query = "SELECT \"idemploye\", SUM(EXTRACT(DAY FROM (\"datefinconge\" - \"datedebconge\") + 1)) AS \"TotalJoursConge\"\r\n"
//        		+ "        FROM \"congés\"\r\n"
//        		+ "        GROUP BY \"idemploye\"\r\n" +
//                "ORDER BY AVG(EXTRACT(DAY FROM (\"DateFinConge\" - \"DateDebConge\") + 1)) DESC;";
//
//        
//
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                int idemploye = resultSet.getInt("idemploye");
//                double dureeMoyenneConge = resultSet.getDouble("DureeMoyenneConge");
//                dureeMoyenneCongeParEmploye.put(idemploye, dureeMoyenneConge);
//            }
//        }
//        return dureeMoyenneCongeParEmploye;
//    }  
     
     // Méthode pour calculer la durée moyenne de congé par employé, avec un classement

/*
    public Map<Integer, Integer> dureeMoyenneCongeParEmploye() throws SQLException {
        Map<Integer, Integer> joursCongeParEmploye = new HashMap<>();
        String query = "SELECT \"idemploye\", SUM(EXTRACT(DAY FROM (\"datefinconge\" - \"datedebconge\") + 1)) AS \"TotalJoursConge\" " +
                "FROM \"congés\" " +
                "GROUP BY \"idemploye\"";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idemploye = resultSet.getInt("idemploye");
                int totalJoursConge = resultSet.getInt("TotalJoursConge");
                joursCongeParEmploye.put(idemploye, totalJoursConge);
            }
        }
        return joursCongeParEmploye;
    }  */
     /*
     // Méthode pour calculer la durée moyenne de congé par employé, avec un classement
     public Map<String, Double> getAverageLeaveDurationByEmployee2() throws SQLException {
         Map<String, Double> averageLeaveDuration = new HashMap<>();
         String query = "SELECT employé.nomemploye, AVG(DATEDIFF(datefinconge, datedebconge)) " +
                 "FROM employé " +
                 "INNER JOIN prendre ON employé.idemploye = prendre.idemploye " +
                 "INNER JOIN congés ON prendre.idconge = congés.idconge " +
                 "GROUP BY employé.nomemploye";
         
         
         try (Statement stmt = connection.createStatement();
              ResultSet rs = stmt.executeQuery(query)) {
             while (rs.next()) {
                 String employeeName = rs.getString(1);
                 double avgDuration = rs.getDouble(2);
                 averageLeaveDuration.put(employeeName, avgDuration);
             }
         }
 		return averageLeaveDuration;
     }
    */
     
     public Map<String, Double> getAverageLeaveDurationByEmployee2(Connection connection) {
    	    Map<String, Double> averageLeaveDuration = new HashMap<>();
    	    String query = "SELECT employé.nomemploye, AVG(DATEDIFF(datefinconge, datedebconge)) " +
    	                   "FROM employé " +
    	                   "INNER JOIN prendre ON employé.idemploye = prendre.idemploye " +
    	                   "INNER JOIN congés ON prendre.idconge = congés.idconge " +
    	                   "GROUP BY employé.nomemploye";

    	    try (Statement stmt = connection.createStatement();
    	         ResultSet rs = stmt.executeQuery(query)) {
    	        while (rs.next()) {
    	            String employeeName = rs.getString(1);
    	            double avgDuration = rs.getDouble(2);
    	            averageLeaveDuration.put(employeeName, avgDuration);
    	        }
    	    } catch (SQLException e) {
    	        // Gérer l'exception
    	        e.printStackTrace();
    	    }

    	    return averageLeaveDuration;
    	}




     public Map<Integer, Double> pourcentageSalaireParRapportMoyenneentreprise() throws SQLException {
    	    Map<Integer, Double> pourcentagesSalaire = new HashMap<>();
    	    String query = "SELECT e.\"idemploye\", AVG(s.\"montantsalaire\") AS \"SalaireMoyen\", AVG(e2.\"salaireemploye\") AS \"Moyenneentreprise\" " +
    	            "FROM \"employé\" e " +
    	            "JOIN \"salaire\" s ON e.\"idemploye\" = s.\"idemploye\" " +
    	            "JOIN \"entreprise\" et ON e.\"idets\" = et.\"idets\" " +
    	            "JOIN \"employé\" e2 ON e.\"idets\" = e2.\"idets\" " +
    	            "GROUP BY e.\"idemploye\"";
    	    
    	    try (PreparedStatement statement = connection.prepareStatement(query)) {
    	        ResultSet resultSet = statement.executeQuery();

    	        while (resultSet.next()) {
    	            int idemploye = resultSet.getInt("idemploye");
    	            BigDecimal salaire = resultSet.getBigDecimal("SalaireMoyen");
    	            BigDecimal moyenneentreprise = resultSet.getBigDecimal("Moyenneentreprise");

    	            double pourcentage = (salaire.doubleValue() / moyenneentreprise.doubleValue()) * 100;
    	            pourcentagesSalaire.put(idemploye, pourcentage);
    	        }
    	    }
    	    return pourcentagesSalaire;
    	}
     
     
     // TOTAL BONUUUUUUUS
     // Méthode pour obtenir la liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus
     public Map<String, BigDecimal> getEmployeesWithTotalBonus2() throws SQLException {
         Map<String, BigDecimal> employeeBonuses = new HashMap<>();
         String query = "SELECT employé.nomemploye, SUM(bonus.montantbonus) " +
                 "FROM employé " +
                 "INNER JOIN prendre ON employé.idemploye = prendre.idemploye " +
                 "INNER JOIN bonus ON employé.idemploye = bonus.idemploye " +
                 "GROUP BY employé.nomemploye";
         try (Statement stmt = connection.createStatement();
              ResultSet rs = stmt.executeQuery(query)) {
             while (rs.next()) {
                 String employeeName = rs.getString(1);
                 BigDecimal totalBonus = rs.getBigDecimal(2);
                 employeeBonuses.put(employeeName, totalBonus);
             }
         }
         return employeeBonuses;
     }
     
    
     
  // Méthode pour calculer la durée moyenne de congé par employé
     public static Map<Integer, Double> calculateAverageLeaveDuration(Connection connection) {
         Map<Integer, Double> averageLeaveDurationMap = new HashMap<>();

         String sqlQuery = "SELECT e.idemploye, CONCAT(e.prenomemploye, ' ', e.nomemploye) AS NomComplet, AVG(c.datefinconge - c.datedebconge) AS DureeMoyenneConge\r\n"
         		+ "FROM employé e\r\n"
         		+ "JOIN prendre p ON e.idemploye = p.idemploye\r\n"
         		+ "JOIN congés c ON p.idconge = c.idconge\r\n"
         		+ "GROUP BY e.Idemploye, nomComplet\r\n"
         		+ "ORDER BY DureeMoyenneConge DESC;";

         try (Statement statement = connection.createStatement()) {
             ResultSet resultSet = statement.executeQuery(sqlQuery);

             while (resultSet.next()) {
                 int idEmploye = resultSet.getInt("IdEmploye");
                 double averageLeaveDuration = resultSet.getDouble("DureeMoyenneConge");
                 averageLeaveDurationMap.put(idEmploye, averageLeaveDuration);
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         return averageLeaveDurationMap;
     }
     
     
  // Méthode pour obtenir la liste des employés qui ont pris des congés et des bonus dans la même période
     public static List<String> getEmployeesWithSamePeriod(Connection connection) {
    	    List<String> employeesWithSamePeriod = new ArrayList<>();

    	    String sqlQuery = "SELECT e.\"idemploye\", \r\n" +
    	            "       CONCAT_WS(' ', e.\"prenomemploye\", e.\"nomemploye\") AS \"NomComplet\"\r\n" +
    	            "FROM \"employé\" e\r\n" +
    	            "JOIN \"prendre\" p ON e.\"idemploye\" = p.\"idemploye\"\r\n" +
    	            "JOIN \"congés\" c ON p.\"idconge\" = c.\"idconge\"\r\n" +
    	            "JOIN \"obtenir\" o ON e.\"idemploye\" = o.\"idemploye\"\r\n" +
    	            "JOIN \"bonus\" b ON o.\"idbonus\" = b.\"idbonus\"\r\n" +
    	            "WHERE (c.\"datedebconge\" <= b.\"datebonus\" AND c.\"datefinconge\" >= b.\"datebonus\")\r\n" +
    	            "      OR (b.\"datebonus\" <= c.\"datefinconge\" AND b.\"datebonus\" >= c.\"datedebconge\")\r\n" +
    	            "";

    	    try (Statement statement = connection.createStatement()) {
    	        ResultSet resultSet = statement.executeQuery(sqlQuery);

    	        while (resultSet.next()) {
    	            int idEmploye = resultSet.getInt("idemploye"); // "idemploye" instead of "IdEmploye"
    	            String nomComplet = resultSet.getString("NomComplet");
    	            employeesWithSamePeriod.add("ID: " + idEmploye + ", Nom: " + nomComplet);
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }

    	    return employeesWithSamePeriod;
    	}

     
     
     
  // Méthode pour obtenir la liste des employés avec le montant total des bonus et la moyenne des salaires
     public static Map<String, Double[]> getEmployeesBonusSalary(Connection connection) {
         Map<String, Double[]> employeesBonusSalary = new HashMap<>();

         // Requête pour récupérer le montant total des bonus et la moyenne des salaires des employés avec congés dans une plage spécifique de dates
         String sqlQuery = "SELECT CONCAT_WS(' ', e.prenomemploye, e.nomemploye) AS NomComplet,\r\n"
         		+ "       SUM(b.montantbonus) AS MontantTotalBonus,\r\n"
         		+ "       AVG(s.montantsalaire) AS MoyenneSalaire\r\n"
         		+ "FROM employé e\r\n"
         		+ "JOIN prendre p ON e.idemploye = p.idemploye\r\n"
         		+ "JOIN congés c ON p.idconge = c.idconge\r\n"
         		+ "JOIN bonus b ON e.idemploye = b.idemploye\r\n"
         		+ "JOIN salaire s ON e.idemploye = s.idemploye\r\n"
         		+ "WHERE c.datedebconge >= DATE '2023-01-01' AND c.datefinconge <= DATE '2023-12-01'\r\n"
         		+ "GROUP BY e.prenomemploye, e.nomemploye;\r\n"
         		+ "";

         try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            
            
             ResultSet resultSet = statement.executeQuery();

             while (resultSet.next()) {
                 String employeeName = resultSet.getString("NomComplet");
                 Double totalBonus = resultSet.getDouble("MontantTotalBonus");
                 Double averageSalary = resultSet.getDouble("MoyenneSalaire");
                 employeesBonusSalary.put(employeeName, new Double[]{totalBonus, averageSalary});
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         return employeesBonusSalary;
     }
     
     //   Méthode pour obtenir le montant total des bonus par mois
     
     public static Map<Date, Double> getMontantTotalBonusParMois(Connection connection) {
         Map<Date, Double> montantTotalBonusParMois = new HashMap<>();

         // Requête pour récupérer le montant total des bonus par mois
         String sqlQuery = "SELECT DATE_TRUNC('month', \"datebonus\") AS \"Mois\",\r\n"
         		+ "       SUM(\"montantbonus\") AS \"MontantTotalBonus\"\r\n"
         		+ "FROM \"bonus\"\r\n"
         		+ "WHERE \"datebonus\" >= DATE_TRUNC('year', CURRENT_DATE - INTERVAL '1 year')\r\n"
         		+ "  AND \"datebonus\" < DATE_TRUNC('year', CURRENT_DATE)\r\n"
         		+ "GROUP BY DATE_TRUNC('month', \"datebonus\")\r\n"
         		+ "ORDER BY DATE_TRUNC('month', \"datebonus\");";

         try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
             ResultSet resultSet = statement.executeQuery();

             while (resultSet.next()) {
                 Date mois = resultSet.getDate("Mois");
                 Double montantTotalBonus = resultSet.getDouble("MontantTotalBonus");
                 montantTotalBonusParMois.put(mois, montantTotalBonus);
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         return montantTotalBonusParMois;
     }
 
 


    
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/bdUSA";
        String user = "postgres";
        String password = "password";
        return DriverManager.getConnection(url, user, password);
        
        
    }


    public static void main(String[] args) {
    	
    	/* try (Connection connection = postgressqlDAO.getConnection()) {
    	        if (connection.isValid(5)) {
    	            System.out.println("Connexion à la base de données établie avec succès.");
    	        } else {
    	            System.out.println("Impossible d'établir une connexion à la base de données.");
    	        }

    	        // Vos autres opérations sur la base de données ici...
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    } */
    	
    	
        try (Connection connection = postgressqlDAO.getConnection()) {
            postgressqlDAO postgressqlDAO = new postgressqlDAO(connection);
            
            

            //Depenses salaires
            BigDecimal depensesSalaires = postgressqlDAO.calculerDepensesSalaires();
            System.out.println("Dépenses totales de salaires pour l'entreprise : " + depensesSalaires);
     

            // Liste des employés avec leur salaire moyen par entreprise
            Map<String, BigDecimal> salaireMoyenParentreprise = postgressqlDAO.salaireMoyenParentreprise();
            System.out.println("Salaire moyen par entreprise : " + salaireMoyenParentreprise);

             // Nombre total de jours de congé par employé //PROBLEME
            //   Map<Integer, Integer> joursCongeParEmploye = postgressqlDAO.nombreTotalJoursCongeParEmploye();
            //   System.out.println("Nombre total de jours de congé par employé : " + joursCongeParEmploye);    
            
         // Liste des employés avec le pourcentage du salaire par rapport à la moyenne de leur entreprise
            Map<Integer, Double> pourcentageSalaireParRapportMoyenneentreprise = postgressqlDAO.pourcentageSalaireParRapportMoyenneentreprise();
            System.out.println("Pourcentage du salaire par rapport à la moyenne de leur entreprise : " + pourcentageSalaireParRapportMoyenneentreprise);
            
            // Liste des employés avec leur salaire moyen par entreprise

         // Liste des employés ayant pris des congés mais n'ayant pas de bonus
            List<Integer> employesSansBonus = postgressqlDAO.employesSansBonus();
            System.out.println("employés ayant pris des congés mais n'ayant pas de bonus : " + employesSansBonus);

          
           
            afficherEmployesAvecCongesEtBonus(connection);
            nombreTotalJoursCongeParEmploye(connection);

            Map<String, BigDecimal> employeesWithTotalBonus = postgressqlDAO.getEmployeesWithTotalBonus2();
            System.out.println("Employees with Total Bonus: " + employeesWithTotalBonus);
          
            
            Map<Integer, Double> averageLeaveDurationMap = calculateAverageLeaveDuration(connection);
            
            // Affichage des résultats
           for (Map.Entry<Integer, Double> entry : averageLeaveDurationMap.entrySet()) {
                int idEmploye = entry.getKey();
                double averageLeaveDuration = entry.getValue();
                System.out.println("Employé avec ID " + idEmploye + " : Moyenne de congé = " + averageLeaveDuration + " jours");
            }
           
        // Appel de la méthode pour obtenir la liste des employés avec des congés et des bonus dans la même période
           List<String> employeesWithSamePeriod = getEmployeesWithSamePeriod(connection);

        // Affichage des résultats
        System.out.println("Liste des employés qui ont pris des congés et des bonus dans la même période :");
        for (String employee : employeesWithSamePeriod) {
            System.out.println(employee);
        }

           
           // Appel de la méthode pour obtenir la liste des employés avec bonus et salaires dans une plage date
           Map<String, Double[]> employeesBonusSalary = getEmployeesBonusSalary(connection);

           // Affichage des résultats
           System.out.println("PLAGE DATE_Liste des employés avec le montant total des bonus et la moyenne des salaires :");
           for (Map.Entry<String, Double[]> entry : employeesBonusSalary.entrySet()) {
               String employeeName = entry.getKey();
               Double[] values = entry.getValue();
               System.out.println("Nom de l'employé : " + employeeName + ", Montant total des bonus : " + values[0] + ", Moyenne des salaires : " + values[1]);
           }
           
           
        // Appel de la méthode pour obtenir le montant total des bonus par mois
           Map<Date, Double> montantTotalBonusParMois = getMontantTotalBonusParMois(connection);

           // Affichage des résultats
           System.out.println("Montant total des bonus distribués chaque mois au cours de l'année précédente :");
           for (Map.Entry<Date, Double> entry : montantTotalBonusParMois.entrySet()) {
               Date mois = entry.getKey();
               Double montantTotalBonus = entry.getValue();
               System.out.println("Mois : " + mois + ", Montant total des bonus : " + montantTotalBonus);
           }
     
            

            
        /*    // Liste des employés qui ont pris des congés et des bonus dans la même période
            List<Integer> employesAvecCongesEtBonusMemePeriode = postgressqlDAO.employesAvecCongesEtBonusMemePeriode();
            System.out.println("employés qui ont pris des congés et des bonus dans la même période : " + employesAvecCongesEtBonusMemePeriode);

            // Liste des employés avec le montant total des bonus et la moyenne de salaire, en incluant uniquement ceux ayant pris des congés dans une plage spécifique de dates
            LocalDate startDate = LocalDate.of(2023, Month.JANUARY, 1);
            LocalDate endDate = LocalDate.of(2023, Month.DECEMBER, 31);
            Map<Integer, Object[]> bonusEtSalaireParEmploye = postgressqlDAO.bonusEtSalaireParEmploye(java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));
            System.out.println("Montant total des bonus et moyenne de salaire : " + bonusEtSalaireParEmploye);

            // Calculer le montant total des bonus distribués chaque mois au cours de l'année précédente
            Map<Month, BigDecimal> montantTotalBonusParMois = postgressqlDAO.montantTotalBonusParMoisAnneePrecedente();
            System.out.println("Montant total des bonus distribués chaque mois au cours de l'année précédente : " + montantTotalBonusParMois);

            
            */
        } catch (SQLException e) {
            e.printStackTrace();
        }
            

         

       
    } 
}