
import ressources.mysql.mysqlDAO;
import ressources.postgressql.postgressqlDAO;
import ressources.csv.CSVDAO;
import ressources.xml.xmlDAO; // Importez la classe xmlDAO



import java.sql.Connection;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
    	
        String dateDebutPlage = "2024-01-01";
        String dateFinPlage = "2024-12-31";
        mysqlDAO mysqlDao = null;
        postgressqlDAO postgressqlDao = null;

        try {
            // Connexion à MySQL
            mysqlDao = new mysqlDAO();
            mysqlDao.connect("jdbc:mysql://localhost:3306/bdfrance1", "root", "");

            // Connexion à PostgreSQL
            Connection postgresConnection = postgressqlDAO.getConnection();
            postgressqlDao = new postgressqlDAO(postgresConnection);
            
            //CSV
            String salairesFilePath = "C:\\Users\\DELL\\Desktop\\csv\\salaires.csv";
            String congesFilePath =  "C:\\Users\\DELL\\Desktop\\csv\\conges.csv";
            String bonusesFilePath = "C:\\Users\\DELL\\Desktop\\csv\\bonuses.csv";

            CSVDAO csvDAO = new CSVDAO(salairesFilePath, congesFilePath, bonusesFilePath);
            
         // XML
            xmlDAO xmlDao = new xmlDAO("C:\\Users\\DELL\\Desktop\\xml\\bdChina.xml");


            // Agrégation des résultats
            BigDecimal totalSalaryExpenses = BigDecimal.ZERO;

            try {
                // Exemple d'exécution de requêtes pour MySQL
                totalSalaryExpenses = totalSalaryExpenses.add(mysqlDao.getTotalSalaryExpenses());
                Map<String, BigDecimal> averageSalaryByCompany = mysqlDao.getAverageSalaryByCompany();
                // Ajoutez d'autres appels de méthode MySQL si nécessaire...

            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                // Exemple d'exécution de requêtes pour PostgreSQL
                totalSalaryExpenses = totalSalaryExpenses.add(postgressqlDao.calculerDepensesSalaires());
                Map<String, BigDecimal> averageSalaryByCompanyPostgres = postgressqlDao.salaireMoyenParentreprise();
                // Ajoutez d'autres appels de méthode PostgreSQL si nécessaire...

            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            //  Utilisation des méthodes du CSVDAO 
            totalSalaryExpenses = totalSalaryExpenses.add(BigDecimal.valueOf(csvDAO.calculerDepensesSalairesEntreprise()));

//            // Appels aux méthodes de xmlDAO
//            xmlDao.afficherSalaireMoyenParEntreprise();
//            xmlDao.calculerDepensesSalairesEntreprise();
//            xmlDao.calculerNombreTotalJoursCongeParEmploye();
//            xmlDao.afficherEmployesCongesSansBonus();
//            xmlDao.afficherEmployesCongesAvecTotalBonus();
//            xmlDao.calculerDureeMoyenneCongeParEmploye();
//            xmlDao.afficherEmployesCongesEtBonusMemePeriode();
//            xmlDao.afficherPourcentageSalaireParRapportMoyenneEntreprise();
//            xmlDao.afficherEmployesAvecBonusEtMoyenneSalaireDansPlageDates(dateDebutPlage, dateFinPlage);
//            xmlDao.calculerMontantTotalBonusParMoisAnneePrecedente();
            
         // Appels aux méthodes de xmlDAO
            double depensesSalairesXML = xmlDao.calculerDepensesSalairesEntreprise(); // Récupérer la valeur de la méthode
            totalSalaryExpenses = totalSalaryExpenses.add(BigDecimal.valueOf(depensesSalairesXML)); // Ajouter la valeur au total existant



            System.out.println("Total Salary Expenses (Combined): " + totalSalaryExpenses);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des connexions à la fin de l'exécution
            try {
                if (mysqlDao != null) {
                    mysqlDao.closeConnection();
                }
                if (postgressqlDao != null) {
                    // Fermeture de la connexion PostgreSQL
                    postgressqlDao.getConnection().close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
