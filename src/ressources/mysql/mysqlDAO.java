package ressources.mysql; 
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.math.*;
import java.time.*;

public class mysqlDAO {
    private Connection connection;
    // Informations de connexion à la base de données MySQL
  
    // Méthode pour obtenir une connexion à la base de données
    public void connect(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    // Méthode pour fermer la connexion à la base de données MySQL
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }




    public BigDecimal getTotalSalaryExpenses() throws SQLException {
        BigDecimal totalExpenses = BigDecimal.ZERO;
        String query = "SELECT SUM(Montant) FROM Salaire";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                totalExpenses = rs.getBigDecimal(1);
            }
        }
        return totalExpenses; 
    }

    // Méthode pour obtenir la liste des employés avec leur salaire moyen par entreprise
    public Map<String, BigDecimal> getAverageSalaryByCompany() throws SQLException {
        Map<String, BigDecimal> averageSalaries = new HashMap<>();
        String query = "SELECT NomEnt, AVG(Salaire) FROM Entreprise INNER JOIN Employe ON Entreprise.IdEnt = Employe.IdEnt GROUP BY NomEnt";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String companyName = rs.getString(1);
                BigDecimal avgSalary = rs.getBigDecimal(2);
                averageSalaries.put(companyName, avgSalary);
            }
        }
        return averageSalaries;
    }

    // Méthode pour obtenir le nombre total de jours de congé par employé
    public Map<String, Integer> getTotalDaysOffByEmployee() throws SQLException {
        Map<String, Integer> totalDaysOff = new HashMap<>();
        String query = "SELECT NomE, COUNT(*) FROM Employe INNER JOIN Prendre ON Employe.IDEmploye = Prendre.IDEmploye INNER JOIN Conges ON Prendre.IDC = Conges.IDC GROUP BY NomE";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String employeeName = rs.getString(1);
                int daysOff = rs.getInt(2);
                totalDaysOff.put(employeeName, daysOff);
            }
        }
        return totalDaysOff;
    }

    // Méthode pour obtenir la liste des employés ayant pris des congés mais n'ayant pas de bonus
    public List<String> getEmployeesWithDaysOffWithoutBonus() throws SQLException {
        List<String> employeesWithoutBonus = new ArrayList<>();
        String query = "SELECT NomE FROM Employe LEFT JOIN Obtenir ON Employe.IDEmploye = Obtenir.IDEmploye WHERE Obtenir.IDB IS NULL AND Employe.IDEmploye IN (SELECT IDEmploye FROM Prendre)";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String employeeName = rs.getString(1);
                employeesWithoutBonus.add(employeeName);
            }
        }
        return employeesWithoutBonus;
    }

    // Méthode pour obtenir la liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus
    public Map<String, BigDecimal> getEmployeesWithTotalBonus() throws SQLException {
        Map<String, BigDecimal> employeeBonuses = new HashMap<>();
        String query = "SELECT Employe.NomE, SUM(Bonus.MontantB) " +
                "FROM Employe " +
                "INNER JOIN Prendre ON Employe.IDEmploye = Prendre.IDEmploye " +
                "INNER JOIN Bonus ON Employe.IDEmploye = Bonus.IDEmploye " +
                "GROUP BY Employe.NomE";
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

    // Méthode pour calculer la durée moyenne de congé par employé, avec un classement
    public Map<String, Double> getAverageLeaveDurationByEmployee() throws SQLException {
        Map<String, Double> averageLeaveDuration = new HashMap<>();
        String query = "SELECT Employe.NomE, AVG(DATEDIFF(DatefinC, DatedebC)) " +
                "FROM Employe " +
                "INNER JOIN Prendre ON Employe.IDEmploye = Prendre.IDEmploye " +
                "INNER JOIN Conges ON Prendre.IDC = Conges.IDC " +
                "GROUP BY Employe.NomE";
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
    
 // Méthode pour calculer le montant total des bonus distribués chaque mois au cours de l'année précédente
    public Map<String, Double> calculateTotalBonusByMonthLastYear(Connection connection) throws SQLException {
        Map<String, Double> totalBonusByMonth = new HashMap<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Préparer la requête SQL
            String query = "SELECT MONTH(DateB) AS Month, YEAR(DateB) AS Year, SUM(MontantB) AS TotalBonus " +
                           "FROM Bonus " +
                           "WHERE YEAR(DateB) = YEAR(CURRENT_DATE()) - 1 " +
                           "GROUP BY YEAR(DateB), MONTH(DateB)";
            preparedStatement = connection.prepareStatement(query);

            // Exécuter la requête
            resultSet = preparedStatement.executeQuery();

            // Parcourir les résultats et stocker le montant total des bonus par mois
            while (resultSet.next()) {
                int month = resultSet.getInt("Month");
                double totalBonus = resultSet.getDouble("TotalBonus");
                totalBonusByMonth.put(month + "/" + resultSet.getInt("Year"), totalBonus);
            }
        } finally {
            // Fermer les ressources
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
        }

        return totalBonusByMonth;
    }
    
    
    // Méthode pour obtenir la liste des employés qui ont pris des congés et des bonus dans la même période
    public List<String> getEmployeesWithSamePeriod() throws SQLException {
        List<String> employeesWithSamePeriod = new ArrayList<>();

        String sqlQuery = "SELECT e.NomE, CONCAT_WS(' ', e.PrenomE, e.NomE) AS NomComplet " +
                "FROM Employe e " +
                "JOIN Prendre p ON e.IDEmploye = p.IDEmploye " +
                "JOIN Conges c ON p.IDC = c.IDC " +
                "JOIN Obtenir o ON e.IDEmploye = o.IDEmploye " +
                "JOIN Bonus b ON o.IDB = b.IDB " +
                "WHERE (c.DateDebC <= b.DateB AND c.DateFinC >= b.DateB) " +
                "      OR (b.DateB <= c.DateFinC AND b.DateB >= c.DateDebC)";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                String nomEmploye = resultSet.getString("NomComplet");
                employeesWithSamePeriod.add(nomEmploye);
            }
        }

        return employeesWithSamePeriod;
    }

    
   
    
    
    
    public static Map<Integer, Map<String, Object>> getEmployesDetails(Connection connexion, String dateDebutConges, String dateFinConges) throws SQLException {
        Map<Integer, Map<String, Object>> employesDetailsMap = new HashMap<>();

        String sqlQuery = "SELECT e.IDEmploye, e.NomE, e.PrenomE,\r\n"
                + "       SUM(b.MontantB) AS MontantTotalBonus,\r\n"
                + "       AVG(s.Montant) AS MoyenneSalaire\r\n"
                + "FROM Employe e\r\n"
                + "INNER JOIN Prendre p ON e.IDEmploye = p.IDEmploye\r\n"
                + "INNER JOIN Conges c ON p.IDC = c.IDC\r\n"
                + "LEFT JOIN Obtenir o ON e.IDEmploye = o.IDEmploye\r\n"
                + "LEFT JOIN Bonus b ON o.IDB = b.IDB\r\n"
                + "LEFT JOIN Salaire s ON e.IDEmploye = s.IDEmploye\r\n"
                + "WHERE c.DatedebC >= ? AND c.DatefinC <= ?\r\n"
                + "GROUP BY e.IDEmploye, e.NomE, e.PrenomE;";

        try (PreparedStatement statement = connexion.prepareStatement(sqlQuery)) {
            statement.setString(1, dateDebutConges);
            statement.setString(2, dateFinConges);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int idEmploye = resultSet.getInt("IDEmploye");
                    String nom = resultSet.getString("NomE");
                    String prenom = resultSet.getString("PrenomE");
                    double montantTotalBonus = resultSet.getDouble("MontantTotalBonus");
                    double moyenneSalaire = resultSet.getDouble("MoyenneSalaire");

                    Map<String, Object> employeDetails = new HashMap<>();
                    employeDetails.put("Nom", nom);
                    employeDetails.put("Prenom", prenom);
                    employeDetails.put("MontantTotalBonus", montantTotalBonus);
                    employeDetails.put("MoyenneSalaire", moyenneSalaire);

                    employesDetailsMap.put(idEmploye, employeDetails);
                }
            }
        }

        return employesDetailsMap;
    }
    
    
    public static Map<Integer, Double> calculerPourcentageSalaireParEmploye(Connection connexion) throws SQLException {
        Map<Integer, Double> pourcentageSalaireParEmploye = new HashMap<>();

        String sqlQuery = "SELECT e.IDEmploye, (s.Montant / m.MoyenneSalaireEntreprise) * 100 AS PourcentageSalaire " +
                          "FROM Employe e " +
                          "INNER JOIN Salaire s ON e.IDEmploye = s.IDEmploye " +
                          "CROSS JOIN (SELECT AVG(s.Montant) AS MoyenneSalaireEntreprise " +
                                      "FROM Salaire s " +
                                      "INNER JOIN Employe e ON s.IDEmploye = e.IDEmploye) m";

        try (Statement statement = connexion.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                int idEmploye = resultSet.getInt("IDEmploye");
                double pourcentageSalaire = resultSet.getDouble("PourcentageSalaire");
                pourcentageSalaireParEmploye.put(idEmploye, pourcentageSalaire);
            }
        }

        return pourcentageSalaireParEmploye;
        
        }
    
    /////reste a extraire les valeurs

    public static Map<String, Double> calculerMontantTotalBonusParMoisMysql(Connection connexion) throws SQLException {
        Map<String, Double> montantTotalBonusParMois = new HashMap<>();

        String sqlQuery = "SELECT MONTH(DateB) AS Mois, SUM(MontantB) AS Montant_Total_Bonus\r\n"
        		+ "FROM Bonus\r\n"
        		+ "WHERE YEAR(DateB) = YEAR(CURRENT_DATE) - 1\r\n"
        		+ "GROUP BY MONTH(DateB);";

        try (Statement statement = connexion.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                String mois = resultSet.getString("Mois");
                double montantTotalBonus = resultSet.getDouble("Montant_Total_Bonus");
                montantTotalBonusParMois.put(mois, montantTotalBonus);
            }
        }

        return montantTotalBonusParMois;
    }
  
    /*
    public static Map<String, Double> calculerMontantTotalBonusParMois(Connection connexion) throws SQLException {
        Map<String, Double> montantTotalBonusParMois = new HashMap<>();

        String sqlQuery = "SELECT YEAR(DateB) AS Annee, MONTH(DateB) AS Mois, SUM(MontantB) AS MontantTotalBonus " +
                          "FROM Bonus " +
                          "WHERE YEAR(DateB) = YEAR(CURRENT_DATE()) - 1 " +
                          "GROUP BY YEAR(DateB), MONTH(DateB)";

        try (Statement statement = connexion.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                int annee = resultSet.getInt("Annee");
                int mois = resultSet.getInt("Mois");
                String cleMois = String.format("%d-%02d", annee, mois);
                double montantTotalBonus = resultSet.getDouble("MontantTotalBonus");
                montantTotalBonusParMois.put(cleMois, montantTotalBonus);
            }
        }

        return montantTotalBonusParMois;
    } */
    
    
    public Connection getConnection() {
        return connection;
    }

    

    

    // MAIN
public static void main(String[] args) {
        mysqlDAO dao = new mysqlDAO();

        // URL de connexion à la base de données MySQL
        String url = "jdbc:mysql://localhost:3306/bdfrance1";
        // Nom d'utilisateur de la base de données MySQL
        String user = "root";
        // Mot de passe de la base de données MySQL
        String password = "";
        



        try {
            // Connexion à la base de données
            dao.connect(url, user, password);
            
            // Dates de début et de fin des congés
            String dateDebutConges = "2024-05-20";
            String dateFinConges = "2024-05-25";

            // Test des différentes méthodes

            // Méthode pour obtenir les dépenses totales de salaires pour l'entreprise
            BigDecimal totalSalaryExpenses = dao.getTotalSalaryExpenses();
            System.out.println("Total Salary Expenses: " + totalSalaryExpenses);

            // Méthode pour obtenir la liste des employés avec leur salaire moyen par entreprise
            Map<String, BigDecimal> averageSalaryByCompany = dao.getAverageSalaryByCompany();
            System.out.println("Average Salary by Company: " + averageSalaryByCompany);

            // Méthode pour obtenir le nombre total de jours de congé par employé
            Map<String, Integer> totalDaysOffByEmployee = dao.getTotalDaysOffByEmployee();
            System.out.println("Total Days Off by Employee: " + totalDaysOffByEmployee);

            // Méthode pour obtenir la liste des employés ayant pris des congés mais n'ayant pas de bonus
            System.out.println("Employees with Days Off Without Bonus: " + dao.getEmployeesWithDaysOffWithoutBonus());

            // Méthode pour obtenir la liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus
            Map<String, BigDecimal> employeesWithTotalBonus = dao.getEmployeesWithTotalBonus();
            System.out.println("Employees with Total Bonus: " + employeesWithTotalBonus);

            // Méthode pour calculer la durée moyenne de congé par employé, avec un classement
            Map<String, Double> averageLeaveDurationByEmployee = dao.getAverageLeaveDurationByEmployee();
            System.out.println("Average Leave Duration by Employee: " + averageLeaveDurationByEmployee);
            
            // Appel de la méthode pour obtenir la liste des employés avec des congés et des bonus dans la même période
            List<String> employeesWithSamePeriod = dao.getEmployeesWithSamePeriod();
            System.out.println("Employees with same period: " + employeesWithSamePeriod);

            Map<Integer, Map<String, Object>> employesDetails = mysqlDAO.getEmployesDetails(dao.getConnection(), dateDebutConges, dateFinConges);

            // Affichage des détails des employés
            for (Map.Entry<Integer, Map<String, Object>> entry : employesDetails.entrySet()) {
                Integer idEmploye = entry.getKey();
                Map<String, Object> details = entry.getValue();
                System.out.println("ID Employé: " + idEmploye);
                System.out.println("Détails: " + details);
            }
            
            
            try {
                Map<Integer, Double> pourcentageSalaireParEmploye = calculerPourcentageSalaireParEmploye(dao.getConnection());
                for (Map.Entry<Integer, Double> entry : pourcentageSalaireParEmploye.entrySet()) {
                    System.out.println("France_ID Employe : " + entry.getKey() + ", Pourcentage du salaire : " + entry.getValue() + "%");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            /////reste a extraire les valeurs
            try {
                Map<String, Double> montantTotalBonusParMois = calculerMontantTotalBonusParMoisMysql(dao.getConnection());
                for (Map.Entry<String, Double> entry : montantTotalBonusParMois.entrySet()) {
                    System.out.println("YOOO Mois : " + entry.getKey() + ", Montant total des bonus _FRANCE " + entry.getValue());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
  
   /*         
         /// Appelez ensuite la méthode sans fournir la connexion à la base de données
            Map<String, Double> TotalBonusByMonthLastYear = dao.calculateTotalBonusByMonthLastYear();
            System.out.println("Total Bonus by MONTH LAST YEAR: " + TotalBonusByMonthLastYear);
*/

            // Fermeture de la connexion
            dao.closeConnection(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
}
