package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Entreprise;

public class EntrepriseDAOImpl implements EntrepriseDAO {

    // Méthode pour obtenir une connexion à la base de données
	private Connection getConnection() throws SQLException {
	    String url = "jdbc:mysql://localhost:3306/bdfrance";
	    String username = "root";
	    String password = "";
	    return DriverManager.getConnection(url, username, password);
	}

    @Override
    public double getTotalSalaryExpenses() {
        double totalExpenses = 0.0;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT SUM(SalaireEmploye) FROM Employe")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalExpenses = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return totalExpenses;
    }

    @Override
    public List<Double> getAverageSalaryByCompany() {
        List<Double> averageSalaries = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT AVG(SalaireEmploye) FROM Employe GROUP BY IdEts")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                averageSalaries.add(rs.getDouble(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return averageSalaries;
    }

    @Override
    public int getTotalVacationDaysByEmployee(int idEmploye) {
        int totalVacationDays = 0;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM prendre WHERE IdEmploye = ?")) {
            stmt.setInt(1, idEmploye);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalVacationDays = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return totalVacationDays;
    }

    @Override
    public List<Entreprise> getEmployeesWithNoBonus() {
        List<Entreprise> employeesWithoutBonus = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Employe WHERE IdEmploye NOT IN (SELECT IdEmploye FROM Obtenir)")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Entreprise entreprise = new Entreprise();
                // Code pour récupérer les données de l'employé à partir du résultat de la requête
                employeesWithoutBonus.add(entreprise);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return employeesWithoutBonus;
    }

    // Implémentez d'autres méthodes nécessaires pour répondre aux requêtes restantes

}
