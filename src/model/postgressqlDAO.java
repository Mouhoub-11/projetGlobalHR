package model;

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
    
    public void insererEmploye(String nom, String prenom, BigDecimal salaire, int age, String tel, int idEts) throws SQLException {
        String query = "INSERT INTO \"Employé\" (\"NomEmploye\", \"PrenomEmploye\", \"SalaireEmploye\", \"AgeEmploye\", \"TelEmploye\", \"IdEts\") VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setBigDecimal(3, salaire);
            statement.setInt(4, age);
            statement.setString(5, tel);
            statement.setInt(6, idEts);
            statement.executeUpdate();
        }
    }

    public void modifierEmploye(int idEmploye, String nouveauNom, String nouveauPrenom, BigDecimal nouveauSalaire, int nouvelAge, String nouveauTel, int nouvelIdEts) throws SQLException {
        String query = "UPDATE \"Employé\" SET \"NomEmploye\" = ?, \"PrenomEmploye\" = ?, \"SalaireEmploye\" = ?, \"AgeEmploye\" = ?, \"TelEmploye\" = ?, \"IdEts\" = ? WHERE \"IdEmploye\" = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nouveauNom);
            statement.setString(2, nouveauPrenom);
            statement.setBigDecimal(3, nouveauSalaire);
            statement.setInt(4, nouvelAge);
            statement.setString(5, nouveauTel);
            statement.setInt(6, nouvelIdEts);
            statement.setInt(7, idEmploye);
            statement.executeUpdate();
        }
    }


    public void supprimerEmploye(int idEmploye) throws SQLException {
        String query = "DELETE FROM \"Employé\" WHERE \"IdEmploye\" = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idEmploye);
            statement.executeUpdate();
        }
    }


     public BigDecimal calculerDepensesSalaires() throws SQLException {
        BigDecimal depensesTotales = BigDecimal.ZERO;
        String query = "SELECT SUM(\"MontantSalaire\") AS \"TotalSalaires\" FROM salaire";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                depensesTotales = resultSet.getBigDecimal("TotalSalaires");
            }
        }
        return depensesTotales;
    }

     public Map<String, BigDecimal> salaireMoyenParEntreprise() throws SQLException {
        Map<String, BigDecimal> salairesMoyens = new HashMap<>();

        String query = "SELECT et.\"NomEts\", AVG(s.\"MontantSalaire\") AS \"SalaireMoyen\" " +
                "FROM \"Employé\" e " +
                "JOIN \"Entreprise\" et ON e.\"IdEts\" = et.\"IdEts\" " +
                "JOIN \"salaire\" s ON e.\"IdEmploye\" = s.\"IdEmploye\" " +
                "GROUP BY et.\"IdEts\"";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String entreprise = resultSet.getString("NomEts");
                BigDecimal salaireMoyen = resultSet.getBigDecimal("SalaireMoyen");
                salairesMoyens.put(entreprise, salaireMoyen);
            }
        }
        return salairesMoyens;
    }



    public Map<Integer, Integer> nombreTotalJoursCongeParEmploye() throws SQLException {
        Map<Integer, Integer> joursCongeParEmploye = new HashMap<>();
        String query = "SELECT \"IdEmploye\", SUM(EXTRACT(DAY FROM (\"DateFinConge\" - \"DateDebConge\") + 1)) AS \"TotalJoursConge\" " +
                "FROM \"Congés\" " +
                "GROUP BY \"IdEmploye\"";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idEmploye = resultSet.getInt("IdEmploye");
                int totalJoursConge = resultSet.getInt("TotalJoursConge");
                joursCongeParEmploye.put(idEmploye, totalJoursConge);
            }
        }
        return joursCongeParEmploye;
    }

    public List<Integer> employesSansBonus() throws SQLException {
        List<Integer> employesSansBonus = new ArrayList<>();
        String query = "SELECT p.\"IdEmploye\" " +
                "FROM \"prendre\" p " +
                "LEFT JOIN \"Obtenir\" o ON p.\"IdEmploye\" = o.\"IdEmploye\" " +
                "WHERE o.\"IdEmploye\" IS NULL";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idEmploye = resultSet.getInt("IdEmploye");
                employesSansBonus.add(idEmploye);
            }
        }
        return employesSansBonus;
    }

     public List<Integer> employesAvecCongesEtBonus() throws SQLException {
        List<Integer> employesAvecCongesEtBonus = new ArrayList<>();
        String query = "SELECT DISTINCT p.\"IdEmploye\" " +
                "FROM \"prendre\" p " +
                "JOIN \"Obtenir\" o ON p.\"IdEmploye\" = o.\"IdEmploye\" " +
                "JOIN \"Congés\" c ON p.\"IdEmploye\" = c.\"IdEmploye\" " +
                "JOIN \"Bonus\" b ON o.\"IdBonus\" = b.\"IdBonus\" " +
                "WHERE c.\"DateDebConge\" <= b.\"DateBonus\" AND c.\"DateFinConge\" >= b.\"DateBonus\"";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idEmploye = resultSet.getInt("IdEmploye");
                employesAvecCongesEtBonus.add(idEmploye);
            }
        }
        return employesAvecCongesEtBonus;
    }
    
    public Map<Integer, Double> dureeMoyenneCongeParEmploye() throws SQLException {
        Map<Integer, Double> dureeMoyenneCongeParEmploye = new HashMap<>();
        String query = "SELECT \"IdEmploye\", AVG(EXTRACT(DAY FROM (\"DateFinConge\" - \"DateDebConge\") + 1)) AS \"DureeMoyenneConge\" " +
                "FROM \"Congés\" " +
                "GROUP BY \"IdEmploye\" " +
                "ORDER BY AVG(EXTRACT(DAY FROM (\"DateFinConge\" - \"DateDebConge\") + 1)) DESC";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idEmploye = resultSet.getInt("IdEmploye");
                double dureeMoyenneConge = resultSet.getDouble("DureeMoyenneConge");
                dureeMoyenneCongeParEmploye.put(idEmploye, dureeMoyenneConge);
            }
        }
        return dureeMoyenneCongeParEmploye;
    }


    public Map<Integer, Double> pourcentageSalaireParRapportMoyenneEntreprise() throws SQLException {
        Map<Integer, Double> pourcentagesSalaire = new HashMap<>();
        String query = "SELECT e.\"IdEmploye\", s.\"MontantSalaire\", AVG(e2.\"SalaireEmploye\") AS \"MoyenneEntreprise\" " +
                "FROM \"Employé\" e " +
                "JOIN \"salaire\" s ON e.\"IdEmploye\" = s.\"IdEmploye\" " +
                "JOIN \"Entreprise\" et ON e.\"IdEts\" = et.\"IdEts\" " +
                "JOIN \"Employé\" e2 ON e.\"IdEts\" = e2.\"IdEts\" " +
                "GROUP BY e.\"IdEmploye\"";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idEmploye = resultSet.getInt("IdEmploye");
                BigDecimal salaire = resultSet.getBigDecimal("MontantSalaire");
                BigDecimal moyenneEntreprise = resultSet.getBigDecimal("MoyenneEntreprise");

                double pourcentage = (salaire.doubleValue() / moyenneEntreprise.doubleValue()) * 100;
                pourcentagesSalaire.put(idEmploye, pourcentage);
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

            BigDecimal depensesSalaires = postgressqlDAO.calculerDepensesSalaires();
            System.out.println("Dépenses totales de salaires pour l'entreprise : " + depensesSalaires);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
            

   /*         // Liste des employés avec leur salaire moyen par entreprise
            Map<String, BigDecimal> salaireMoyenParEntreprise = postgressqlDAO.salaireMoyenParEntreprise();
            System.out.println("Salaire moyen par entreprise : " + salaireMoyenParEntreprise);

            // Nombre total de jours de congé par employé
            Map<Integer, Integer> joursCongeParEmploye = postgressqlDAO.nombreTotalJoursCongeParEmploye();
            System.out.println("Nombre total de jours de congé par employé : " + joursCongeParEmploye);
            // Liste des employés avec leur salaire moyen par entreprise
            

     

            // Liste des employés ayant pris des congés mais n'ayant pas de bonus
            List<Integer> employesSansBonus = postgressqlDAO.employesSansBonus();
            System.out.println("Employés ayant pris des congés mais n'ayant pas de bonus : " + employesSansBonus);

            // Liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus
            List<Integer> employesAvecCongesEtBonus = postgressqlDAO.employesAvecCongesEtBonus();
            System.out.println("Employés ayant pris des congés et le total des bonus qu'ils ont reçus : " + employesAvecCongesEtBonus);

            // Calcul de la durée moyenne de congé par employé, avec un classement
            Map<Integer, Double> dureeMoyenneCongeParEmploye = postgressqlDAO.dureeMoyenneCongeParEmploye();
            System.out.println("Durée moyenne de congé par employé : " + dureeMoyenneCongeParEmploye);  */

            // Liste des employés qui ont pris des congés et des bonus dans la même période
       /*     List<Integer> employesAvecCongesEtBonusMemePeriode = postgressqlDAO.employesAvecCongesEtBonusMemePeriode();
            System.out.println("Employés qui ont pris des congés et des bonus dans la même période : " + employesAvecCongesEtBonusMemePeriode);

            // Liste des employés avec le montant total des bonus et la moyenne de salaire, en incluant uniquement ceux ayant pris des congés dans une plage spécifique de dates
            LocalDate startDate = LocalDate.of(2023, Month.JANUARY, 1);
            LocalDate endDate = LocalDate.of(2023, Month.DECEMBER, 31);
            Map<Integer, Object[]> bonusEtSalaireParEmploye = postgressqlDAO.bonusEtSalaireParEmploye(java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));
            System.out.println("Montant total des bonus et moyenne de salaire : " + bonusEtSalaireParEmploye);

            // Calculer le montant total des bonus distribués chaque mois au cours de l'année précédente
            Map<Month, BigDecimal> montantTotalBonusParMois = postgressqlDAO.montantTotalBonusParMoisAnneePrecedente();
            System.out.println("Montant total des bonus distribués chaque mois au cours de l'année précédente : " + montantTotalBonusParMois);

            // Liste des employés avec le pourcentage du salaire par rapport à la moyenne de leur entreprise
            Map<Integer, Double> pourcentageSalaireParRapportMoyenneEntreprise = postgressqlDAO.pourcentageSalaireParRapportMoyenneEntreprise();
            System.out.println("Pourcentage du salaire par rapport à la moyenne de leur entreprise : " + pourcentageSalaireParRapportMoyenneEntreprise);

       */
    } 
}