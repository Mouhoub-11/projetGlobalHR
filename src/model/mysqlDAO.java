package model; 
import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    
//    public void insererEmploye(String nom, String prenom, BigDecimal salaire, int age, String tel, int idEts) throws SQLException {
//        String query = "INSERT INTO Employé (NomEmploye, PrenomEmploye, SalaireEmploye, AgeEmploye, TelEmploye, IdEts) VALUES (?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, nom);
//            statement.setString(2, prenom);
//            statement.setBigDecimal(3, salaire);
//            statement.setInt(4, age);
//            statement.setString(5, tel);
//            statement.setInt(6, idEts);
//            statement.executeUpdate();
//        }
//    }
//    
//    public void modifierEmploye(int idEmploye, String nouveauNom, String nouveauPrenom, BigDecimal nouveauSalaire, int nouvelAge, String nouveauTel, int nouvelIdEts) throws SQLException {
//        String query = "UPDATE Employé SET NomEmploye = ?, PrenomEmploye = ?, SalaireEmploye = ?, AgeEmploye = ?, TelEmploye = ?, IdEts = ? WHERE IdEmploye = ?";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, nouveauNom);
//            statement.setString(2, nouveauPrenom);
//            statement.setBigDecimal(3, nouveauSalaire);
//            statement.setInt(4, nouvelAge);
//            statement.setString(5, nouveauTel);
//            statement.setInt(6, nouvelIdEts);
//            statement.setInt(7, idEmploye);
//            statement.executeUpdate();
//        }
//    }
//    
//    public void supprimerEmploye(int idEmploye) throws SQLException {
//        String query = "DELETE FROM Employé WHERE IdEmploye = ?";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, idEmploye);
//            statement.executeUpdate();
//        }
//    }


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

            // Fermeture de la connexion
            dao.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
}
