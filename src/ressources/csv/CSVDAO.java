package ressources.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CSVDAO {
    private String salairesFilePath;
    private String congesFilePath;
    private String bonusesFilePath;

    public CSVDAO(String salairesFilePath, String congesFilePath, String bonusesFilePath) {
        this.salairesFilePath = salairesFilePath;
        this.congesFilePath = congesFilePath;
        this.bonusesFilePath = bonusesFilePath;
    }

    // Calculer les dépenses totales de salaires pour l'entreprise
    public double calculerDepensesSalairesEntreprise() {
        double depensesTotalesSalaires = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader(salairesFilePath))) {
            String line;
            // Ignorer la première ligne qui contient les en-têtes
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double montantSalaire = Double.parseDouble(values[1]); // Le montant du salaire est à l'index 1
                depensesTotalesSalaires += montantSalaire;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return depensesTotalesSalaires;
    }

    // Calculer le salaire moyen par entreprise

    public Map<String, Double> calculerSalaireMoyenParEntreprise() {
        Map<String, Double> salaireMoyenParEntreprise = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(salairesFilePath))) {
            String line;
            br.readLine(); // Ignorer la première ligne qui contient les en-têtes
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String nomEntreprise = values[4]; // L'entreprise est à l'index 4 dans le fichier CSV des salaires
                double montantSalaire = Double.parseDouble(values[1]); // Le montant du salaire est à l'index 1
                if (!salaireMoyenParEntreprise.containsKey(nomEntreprise)) {
                    salaireMoyenParEntreprise.put(nomEntreprise, montantSalaire);
                } else {
                    salaireMoyenParEntreprise.put(nomEntreprise, salaireMoyenParEntreprise.get(nomEntreprise) + montantSalaire);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return salaireMoyenParEntreprise;
    }

    // Calculer le nombre total de jours de congé par employé

    public Map<Integer, Integer> calculerTotalJoursCongeParEmploye() {
        Map<Integer, Integer> joursCongeParEmploye = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(congesFilePath))) {
            String line;
            // Ignorer la première ligne qui contient les en-têtes
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int idEmploye = Integer.parseInt(values[4]); // L'identifiant de l'employé est à l'index 4 dans le fichier CSV des congés
                // Si l'employé est déjà présent dans la map, on ajoute les jours de congé
                // Sinon, on initialise avec les jours de congé
                joursCongeParEmploye.put(idEmploye, joursCongeParEmploye.getOrDefault(idEmploye, 0) + 1);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return joursCongeParEmploye;
    }

    // Calculer le total des bonus par employé

    public Map<Integer, Double> calculerTotalBonusParEmploye() {
        Map<Integer, Double> bonusParEmploye = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(bonusesFilePath))) {
            String line;
            // Ignorer la première ligne qui contient les en-têtes
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int idEmploye = Integer.parseInt(values[4]); // L'identifiant de l'employé est à l'index 4 dans le fichier CSV des bonus
                double montantBonus = Double.parseDouble(values[1]); // Le montant du bonus est à l'index 1
                bonusParEmploye.put(idEmploye, bonusParEmploye.getOrDefault(idEmploye, 0.0) + montantBonus);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return bonusParEmploye;
    }

    // Liste des employés ayant pris des congés mais n'ayant pas de bonus

    public void listeEmployesAvecCongesSansBonus() {
        Map<Integer, Integer> joursCongeParEmploye = calculerTotalJoursCongeParEmploye();
        Map<Integer, Double> bonusParEmploye = calculerTotalBonusParEmploye();

        for (Map.Entry<Integer, Integer> entry : joursCongeParEmploye.entrySet()) {
            int idEmploye = entry.getKey();
            if (!bonusParEmploye.containsKey(idEmploye) || bonusParEmploye.get(idEmploye) == 0.0) {
                System.out.println("Employé avec ID " + idEmploye + " a pris des congés mais n'a pas de bonus.");
            }
        }
    }

    // Liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus

    public void listeEmployesAvecCongesEtBonus() {
        Map<Integer, Integer> joursCongeParEmploye = calculerTotalJoursCongeParEmploye();
        Map<Integer, Double> bonusParEmploye = calculerTotalBonusParEmploye();

        for (Map.Entry<Integer, Integer> entry : joursCongeParEmploye.entrySet()) {
            int idEmploye = entry.getKey();
            if (bonusParEmploye.containsKey(idEmploye) && bonusParEmploye.get(idEmploye) > 0.0) {
                System.out.println("Employé avec ID " + idEmploye + " a pris des congés et a reçu un total de bonus de " + bonusParEmploye.get(idEmploye));
            }
        }
    }

    // Calculer la durée moyenne de congé par employé

    public Map<Integer, Double> calculerDureeMoyenneCongeParEmploye() {
        Map<Integer, Integer> totalJoursCongeParEmploye = new HashMap<>();
        Map<Integer, Integer> nombreCongesParEmploye = new HashMap<>();
        Map<Integer, Double> dureeMoyenneCongeParEmploye = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(congesFilePath))) {
            String line;
            // Ignorer la première ligne qui contient les en-têtes
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int idEmploye = Integer.parseInt(values[4]); // L'identifiant de l'employé est à l'index 4 dans le fichier CSV des congés
                LocalDate dateDebut = LocalDate.parse(values[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Date de début est à l'index 1
                LocalDate dateFin = LocalDate.parse(values[2], DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Date de fin est à l'index 2
                int dureeConge = (int) (dateFin.toEpochDay() - dateDebut.toEpochDay()) + 1;

                totalJoursCongeParEmploye.put(idEmploye, totalJoursCongeParEmploye.getOrDefault(idEmploye, 0) + dureeConge);
                nombreCongesParEmploye.put(idEmploye, nombreCongesParEmploye.getOrDefault(idEmploye, 0) + 1);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Integer, Integer> entry : totalJoursCongeParEmploye.entrySet()) {
            int idEmploye = entry.getKey();
            int totalJoursConge = entry.getValue();
            int nombreConges = nombreCongesParEmploye.get(idEmploye);
            double dureeMoyenneConge = (double) totalJoursConge / nombreConges;
            dureeMoyenneCongeParEmploye.put(idEmploye, dureeMoyenneConge);
        }

        return dureeMoyenneCongeParEmploye;
    }

    // Liste des employés qui ont pris des congés et des bonus dans la même période

    public void listeEmployesAvecCongesEtBonusMemePeriode() {
        Map<Integer, Integer> joursCongeParEmploye = calculerTotalJoursCongeParEmploye();
        Map<Integer, Double> bonusParEmploye = calculerTotalBonusParEmploye();

        try (BufferedReader br = new BufferedReader(new FileReader(congesFilePath))) {
            String line;
            // Ignorer la première ligne qui contient les en-têtes
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int idEmploye = Integer.parseInt(values[4]); // L'identifiant de l'employé est à l'index 4 dans le fichier CSV des congés
                LocalDate dateDebutConge = LocalDate.parse(values[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Date de début est à l'index 1
                LocalDate dateFinConge = LocalDate.parse(values[2], DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Date de fin est à l'index 2

                if (bonusParEmploye.containsKey(idEmploye)) {
                    try (BufferedReader brBonus = new BufferedReader(new FileReader(bonusesFilePath))) {
                        String lineBonus;
                        // Ignorer la première ligne qui contient les en-têtes
                        brBonus.readLine();
                        while ((lineBonus = brBonus.readLine()) != null) {
                            String[] valuesBonus = lineBonus.split(",");
                            int idEmployeBonus = Integer.parseInt(valuesBonus[4]); // L'identifiant de l'employé est à l'index 4 dans le fichier CSV des bonus
                            LocalDate dateBonus = LocalDate.parse(valuesBonus[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Date du bonus est à l'index 3
                            if (idEmploye == idEmployeBonus && (dateBonus.isAfter(dateDebutConge) || dateBonus.isBefore(dateFinConge))) {
                                System.out.println("Employé avec ID " + idEmploye + " a pris des congés et a reçu un bonus dans la même période.");
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Liste des employés avec bonus et salaire moyen dans une plage de dates spécifiée

    public void listeEmployesAvecBonusEtSalaireMoyenDansPlageDates(String dateDebut, String dateFin) {
        LocalDate debutPlage = LocalDate.parse(dateDebut, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate finPlage = LocalDate.parse(dateFin, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Map<Integer, Double> bonusParEmploye = calculerTotalBonusParEmploye();

        try (BufferedReader br = new BufferedReader(new FileReader(salairesFilePath))) {
            String line;
            // Ignorer la première ligne qui contient les en-têtes
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int idEmploye = Integer.parseInt(values[0]); // L'identifiant de l'employé est à l'index 0 dans le fichier CSV des salaires
                double salaire = Double.parseDouble(values[1]); // Le montant du salaire est à l'index 1
                LocalDate dateSalaire = LocalDate.parse(values[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Date du salaire est à l'index 3

                if (bonusParEmploye.containsKey(idEmploye) && dateSalaire.isAfter(debutPlage) && dateSalaire.isBefore(finPlage)) {
                    System.out.println("Employé avec ID " + idEmploye + " a reçu un bonus de " + bonusParEmploye.get(idEmploye) + " et a un salaire moyen de " + salaire + " dans la plage de dates spécifiée.");
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Calculer le montant total des bonus distribués chaque mois de l'année précédente

    public Map<String, Double> calculerTotalBonusParMoisAnneePrecedente() {
        Map<String, Double> totalBonusParMois = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(bonusesFilePath))) {
            String line;
            // Ignorer la première ligne qui contient les en-têtes
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double montantBonus = Double.parseDouble(values[1]); // Le montant du bonus est à l'index 1
                LocalDate dateBonus = LocalDate.parse(values[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Date du bonus est à l'index 3
                String moisAnnee = dateBonus.getMonthValue() + "-" + dateBonus.getYear();
                totalBonusParMois.put(moisAnnee, totalBonusParMois.getOrDefault(moisAnnee, 0.0) + montantBonus);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return totalBonusParMois;
    }

    // Liste des employés avec le pourcentage du salaire par rapport à la moyenne de leur entreprise

    public void listeEmployesAvecPourcentageSalaireParRapportMoyenneEntreprise() {
        Map<String, Double> salaireMoyenParEntreprise = calculerSalaireMoyenParEntreprise();

        try (BufferedReader br = new BufferedReader(new FileReader(salairesFilePath))) {
            String line;
            // Ignorer la première ligne qui contient les en-têtes
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String nomEntreprise = values[4]; // L'entreprise est à l'index 4 dans le fichier CSV des salaires
                double montantSalaire = Double.parseDouble(values[1]); // Le montant du salaire est à l'index 1
                double salaireMoyenEntreprise = salaireMoyenParEntreprise.getOrDefault(nomEntreprise, 0.0);
                double pourcentageSalaire = (montantSalaire / salaireMoyenEntreprise) * 100;
                System.out.println("Employé avec ID " + values[0] + " a un salaire de " + montantSalaire + " qui représente " + pourcentageSalaire + "% de la moyenne de son entreprise.");
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Chemins des fichiers CSV
        String salairesFilePath = "C:\\Users\\DELL\\Desktop\\csv\salaires.csv";
        String congesFilePath =  "C:\\Users\\DELL\\Desktop\\csv\\conges.csv";
        String bonusesFilePath = "C:\\Users\\DELL\\Desktop\\csv\bonuses.csv";
        
        // Création de l'instance du DAO
        CSVDAO csvDAO = new CSVDAO(salairesFilePath, congesFilePath, bonusesFilePath);

        // Calcul des dépenses totales de salaires pour l'entreprise
        double depensesTotalesSalaires = csvDAO.calculerDepensesSalairesEntreprise();
        System.out.println("Dépenses totales de salaires pour l'entreprise : " + depensesTotalesSalaires);

        // Calcul de la liste des employés avec leur salaire moyen par entreprise
        Map<String, Double> salaireMoyenParEntreprise = csvDAO.calculerSalaireMoyenParEntreprise();
        System.out.println("Liste des employés avec leur salaire moyen par entreprise :");
        for (Map.Entry<String, Double> entry : salaireMoyenParEntreprise.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        // Calcul du nombre total de jours de congé par employé
        Map<Integer, Integer> joursCongeParEmploye = csvDAO.calculerTotalJoursCongeParEmploye();
        System.out.println("Nombre total de jours de congé par employé :");
        for (Map.Entry<Integer, Integer> entry : joursCongeParEmploye.entrySet()) {
            System.out.println("Employé avec ID " + entry.getKey() + " : " + entry.getValue() + " jours");
        }

        // Liste des employés ayant pris des congés mais n'ayant pas de bonus
        System.out.println("Liste des employés ayant pris des congés mais n'ayant pas de bonus :");
        csvDAO.listeEmployesAvecCongesSansBonus();

        // Liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus
        System.out.println("Liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus :");
        csvDAO.listeEmployesAvecCongesEtBonus();

        // Calcul de la durée moyenne de congé par employé, avec un classement
        Map<Integer, Double> dureeMoyenneCongeParEmploye = csvDAO.calculerDureeMoyenneCongeParEmploye();
        System.out.println("Durée moyenne de congé par employé (en jours) :");
        for (Map.Entry<Integer, Double> entry : dureeMoyenneCongeParEmploye.entrySet()) {
            System.out.println("Employé avec ID " + entry.getKey() + " : " + entry.getValue() + " jours");
        }

        // Liste des employés qui ont pris des congés et des bonus dans la même période
        System.out.println("Liste des employés qui ont pris des congés et des bonus dans la même période :");
        csvDAO.listeEmployesAvecCongesEtBonusMemePeriode();

        // Liste des employés avec le montant total des bonus et la moyenne de salaire
        // en incluant uniquement ceux ayant pris des congés dans une plage spécifique de dates
        String dateDebut = "2023-01-01";
        String dateFin = "2023-12-31";
        System.out.println("Liste des employés avec bonus et salaire moyen dans la plage de dates spécifiée :");
        csvDAO.listeEmployesAvecBonusEtSalaireMoyenDansPlageDates(dateDebut, dateFin);

        // Calcul du montant total des bonus distribués chaque mois au cours de l'année précédente
        Map<String, Double> totalBonusParMoisAnneePrecedente = csvDAO.calculerTotalBonusParMoisAnneePrecedente();
        System.out.println("Montant total des bonus distribués chaque mois de l'année précédente :");
        for (Map.Entry<String, Double> entry : totalBonusParMoisAnneePrecedente.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        // Liste des employés avec le pourcentage du salaire par rapport à la moyenne de leur entreprise
        System.out.println("Liste des employés avec le pourcentage du salaire par rapport à la moyenne de leur entreprise :");
        csvDAO.listeEmployesAvecPourcentageSalaireParRapportMoyenneEntreprise();
    }
}
