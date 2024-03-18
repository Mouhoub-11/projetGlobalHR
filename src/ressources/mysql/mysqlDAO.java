package ressources.mysql; 
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

import model.postgressqlDAO;

public class mysqlDAO {
    private Connection connection;
    // Informations de connexion à la base de données MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/bdfrance";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Méthode pour obtenir une connexion à la base de données
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Chargement du driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Établissement de la connexion
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    
    public void insererEmploye(String nom, String prenom, BigDecimal salaire, int age, String tel, int idEts) throws SQLException {
        String query = "INSERT INTO Employé (NomEmploye, PrenomEmploye, SalaireEmploye, AgeEmploye, TelEmploye, IdEts) VALUES (?, ?, ?, ?, ?, ?)";
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
        String query = "UPDATE Employé SET NomEmploye = ?, PrenomEmploye = ?, SalaireEmploye = ?, AgeEmploye = ?, TelEmploye = ?, IdEts = ? WHERE IdEmploye = ?";
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
        String query = "DELETE FROM Employé WHERE IdEmploye = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idEmploye);
            statement.executeUpdate();
        }
    }




    
    public BigDecimal calculerDepensesSalaires() throws SQLException {
        BigDecimal depensesTotales = BigDecimal.ZERO;
        String query = "SELECT SUM(MontantSalaire) AS TotalSalaires FROM salaire";
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
        String query = "SELECT e.NomEts, AVG(s.MontantSalaire) AS SalaireMoyen " +
                "FROM Employé e " +
                "JOIN Entreprise et ON e.IdEts = et.IdEts " +
                "JOIN salaire s ON e.IdEmploye = s.IdEmploye " +
                "GROUP BY e.IdEts";
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
        String query = "SELECT IdEmployé, SUM(DATEDIFF(DateFinConge, DateDebConge) + 1) AS TotalJoursConge " +
                "FROM Congés " +
                "GROUP BY IdEmploye";
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
        String query = "SELECT p.IdEmploye " +
                "FROM prendre p " +
                "LEFT JOIN Obtenir o ON p.IdEmploye = o.IdEmploye " +
                "WHERE o.IdEmploye IS NULL";
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
        String query = "SELECT DISTINCT p.IdEmploye " +
                "FROM prendre p " +
                "JOIN Obtenir o ON p.IdEmploye = o.IdEmploye" +
                "JOIN Congés c ON p.IdEmployé = c.IdEmployé " +
                "JOIN Bonus b ON o.IdBonus = b.IdBonus " +
                "WHERE c.DateDebConge <= b.DateBonus AND c.DateFinConge >= b.DateBonus";
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
        String query = "SELECT IdEmploye, AVG(DATEDIFF(DateFinConge, DateDebConge) + 1) AS DureeMoyenneConge " +
                "FROM Congés " +
                "GROUP BY IdEmploye " +
                "ORDER BY AVG(DATEDIFF(DateFinConge, DateDebConge) + 1) DESC";
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

    public List<Integer> employesAvecCongesEtBonusMemePeriode() throws SQLException {
        List<Integer> employesAvecCongesEtBonus = new ArrayList<>();
        String query = "SELECT DISTINCT p.IdEmploye " +
                "FROM prendre p " +
                "JOIN Obtenir o ON p.IdEmploye = o.IdEmploye " +
                "JOIN Congés c ON p.IdEmploye = c.IdEmploye " +
                "JOIN Bonus b ON o.IdBonus = b.IdBonus " +
                "WHERE c.DateDebConge <= b.DateBonus AND c.DateFinConge >= b.DateBonus";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idEmploye = resultSet.getInt("IdEmploye");
                employesAvecCongesEtBonus.add(idEmploye);
            }
        }
        return employesAvecCongesEtBonus;
    }

    public Map<Integer, Object[]> bonusEtSalaireParEmploye(Date startDate, Date endDate) throws SQLException {
        Map<Integer, Object[]> bonusEtSalaireParEmploye = new HashMap<>();
        String query = "SELECT e.IdEmploye, SUM(b.MontantBonus) AS TotalBonus, AVG(s.MontantSalaire) AS SalaireMoyen " +
                "FROM Employe e " +
                "LEFT JOIN Obtenir o ON e.IdEmploye = o.IdEmploye" +
                "LEFT JOIN Bonus b ON o.IdBonus = b.IdBonus " +
                "LEFT JOIN salaire s ON e.IdEmploye = s.IdEmploye " +
                "LEFT JOIN Congés c ON e.IdEmploye = c.IdEmploye " +
                "WHERE c.DateDebConge BETWEEN ? AND ? " +
                "GROUP BY e.IdEmploye";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, startDate);
            statement.setDate(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idEmploye = resultSet.getInt("IdEmploye");
                BigDecimal totalBonus = resultSet.getBigDecimal("TotalBonus");
                BigDecimal salaireMoyen = resultSet.getBigDecimal("SalaireMoyen");
                Object[] data = {totalBonus, salaireMoyen};
                bonusEtSalaireParEmploye.put(idEmploye, data);
            }
        }
        return bonusEtSalaireParEmploye;
    }

    public Map<Month, BigDecimal> montantTotalBonusParMoisAnneePrecedente() throws SQLException {
        Map<Month, BigDecimal> montantTotalBonusParMois = new HashMap<>();
        // Obtention de la date du premier jour de l'année précédente
        LocalDate startDate = LocalDate.now().minusYears(1).withMonth(1).withDayOfMonth(1);
        // Obtention de la date du dernier jour de l'année précédente
        LocalDate endDate = startDate.withMonth(12).withDayOfMonth(31);

        String query = "SELECT MONTH(DateBonus) AS Mois, SUM(MontantBonus) AS TotalBonus " +
                "FROM Bonus " +
                "WHERE DateBonus BETWEEN ? AND ? " +
                "GROUP BY MONTH(DateBonus)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, java.sql.Date.valueOf(startDate));
            statement.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int mois = resultSet.getInt("Mois");
                BigDecimal montantTotal = resultSet.getBigDecimal("TotalBonus");
                montantTotalBonusParMois.put(Month.of(mois), montantTotal);
            }
        }
        return montantTotalBonusParMois;
    }

    public Map<Integer, Double> pourcentageSalaireParRapportMoyenneEntreprise() throws SQLException {
        Map<Integer, Double> pourcentagesSalaire = new HashMap<>();
        String query = "SELECT e.IdEmploye, s.MontantSalaire, AVG(e2.SalaireEmploye) AS MoyenneEntreprise " +
                "FROM Employé e " +
                "JOIN salaire s ON e.IdEmploye = s.IdEmploye " +
                "JOIN Entreprise et ON e.IdEts = et.IdEts " +
                "JOIN Employé e2 ON e.IdEts = e2.IdEts " +
                "GROUP BY e.IdEmploye";
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

    // Exemple d'utilisation de la connexion
    public static void main(String[] args) {
        try (Connection connection = mysqlDAO.getConnection()) {
            mysqlDAO employeDAO = new mysqlDAO(connection);

            // Dépenses totales de salaires pour l'entreprise
            BigDecimal depensesSalaires = employeDAO.calculerDepensesSalaires();
            System.out.println("Dépenses totales de salaires pour l'entreprise : " + depensesSalaires);

            // Liste des employés avec leur salaire moyen par entreprise
            Map<String, BigDecimal> salaireMoyenParEntreprise = employeDAO.salaireMoyenParEntreprise();
            System.out.println("Salaire moyen par entreprise : " + salaireMoyenParEntreprise);

            // Nombre total de jours de congé par employé
            Map<Integer, Integer> joursCongeParEmploye = employeDAO.nombreTotalJoursCongeParEmploye();
            System.out.println("Nombre total de jours de congé par employé : " + joursCongeParEmploye);

            // Liste des employés ayant pris des congés mais n'ayant pas de bonus
            List<Integer> employesSansBonus = employeDAO.employesSansBonus();
            System.out.println("Employés ayant pris des congés mais n'ayant pas de bonus : " + employesSansBonus);

            // Liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus
            List<Integer> employesAvecCongesEtBonus = employeDAO.employesAvecCongesEtBonus();
            System.out.println("Employés ayant pris des congés et le total des bonus qu'ils ont reçus : " + employesAvecCongesEtBonus);

            // Calcul de la durée moyenne de congé par employé, avec un classement
            Map<Integer, Double> dureeMoyenneCongeParEmploye = employeDAO.dureeMoyenneCongeParEmploye();
            System.out.println("Durée moyenne de congé par employé : " + dureeMoyenneCongeParEmploye);

            // Liste des employés qui ont pris des congés et des bonus dans la même période
            List<Integer> employesAvecCongesEtBonusMemePeriode = employeDAO.employesAvecCongesEtBonusMemePeriode();
            System.out.println("Employés qui ont pris des congés et des bonus dans la même période : " + employesAvecCongesEtBonusMemePeriode);

            // Liste des employés avec le montant total des bonus et la moyenne de salaire, en incluant uniquement ceux ayant pris des congés dans une plage spécifique de dates
            LocalDate startDate = LocalDate.of(2023, Month.JANUARY, 1);
            LocalDate endDate = LocalDate.of(2023, Month.DECEMBER, 31);
            Map<Integer, Object[]> bonusEtSalaireParEmploye = employeDAO.bonusEtSalaireParEmploye(java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));
            System.out.println("Montant total des bonus et moyenne de salaire : " + bonusEtSalaireParEmploye);

            // Calculer le montant total des bonus distribués chaque mois au cours de l'année précédente
            Map<Month, BigDecimal> montantTotalBonusParMois = employeDAO.montantTotalBonusParMoisAnneePrecedente();
            System.out.println("Montant total des bonus distribués chaque mois au cours de l'année précédente : " + montantTotalBonusParMois);

            // Liste des employés avec le pourcentage du salaire par rapport à la moyenne de leur entreprise
            Map<Integer, Double> pourcentageSalaireParRapportMoyenneEntreprise = employeDAO.pourcentageSalaireParRapportMoyenneEntreprise();
            System.out.println("Pourcentage du salaire par rapport à la moyenne de leur entreprise : " + pourcentageSalaireParRapportMoyenneEntreprise);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
        if (connection != null) {
            System.out.println("Connexion réussie !");
            // Vous pouvez exécuter vos requêtes SQL ici
            // N'oubliez pas de fermer la connexion lorsque vous avez fini
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Échec de la connexion.");
        } */
        
        
        
    }
}
