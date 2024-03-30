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
    
    public void insererEmploye(String nom, String prenom, BigDecimal salaire, int age, String tel, int idets) throws SQLException {
        String query = "INSERT INTO \"employé\" (\"NomEmploye\", \"PrenomEmploye\", \"SalaireEmploye\", \"AgeEmploye\", \"TelEmploye\", \"idets\") VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setBigDecimal(3, salaire);
            statement.setInt(4, age);
            statement.setString(5, tel);
            statement.setInt(6, idets);
            statement.executeUpdate();
        }
    }

    public void modifierEmploye(int idemploye, String nouveauNom, String nouveauPrenom, BigDecimal nouveauSalaire, int nouvelAge, String nouveauTel, int nouvelidets) throws SQLException {
        String query = "UPDATE \"employé\" SET \"NomEmploye\" = ?, \"PrenomEmploye\" = ?, \"SalaireEmploye\" = ?, \"AgeEmploye\" = ?, \"TelEmploye\" = ?, \"idets\" = ? WHERE \"idemploye\" = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nouveauNom);
            statement.setString(2, nouveauPrenom);
            statement.setBigDecimal(3, nouveauSalaire);
            statement.setInt(4, nouvelAge);
            statement.setString(5, nouveauTel);
            statement.setInt(6, nouvelidets);
            statement.setInt(7, idemploye);
            statement.executeUpdate();
        }
    }


    public void supprimerEmploye(int idemploye) throws SQLException {
        String query = "DELETE FROM \"employé\" WHERE \"idemploye\" = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idemploye);
            statement.executeUpdate();
        }
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
             System.out.println("\nNombre total de jours de congé par employé :");
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


    /* public Map<Integer, Integer> dureeMoyenneCongeParEmploye() throws SQLException {
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
          

//            // Calcul de la durée moyenne de congé par employé, avec un classement
//            Map<Integer, Double> dureeMoyenneCongeParEmploye = postgressqlDAO.dureeMoyenneCongeParEmploye();
//            System.out.println("Durée moyenne de congé par employé : " + dureeMoyenneCongeParEmploye);  
            
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