package ressources.xml;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Calendar;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.NoSuchElementException;


public class xmlDAO {
    private Scanner scanner;
    private Document document;

    public xmlDAO(String cheminFichier) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse("C:\\Users\\DELL\\Desktop\\xml\\bdChina.xml");
            this.scanner = new Scanner(System.in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 public int calculerDifferenceJours(String dateDebutStr, String dateFinStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateDebut = sdf.parse(dateDebutStr);
            Date dateFin = sdf.parse(dateFinStr);

            long differenceMillis = dateFin.getTime() - dateDebut.getTime();
            return (int) (differenceMillis / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Gérer l'erreur de conversion de date
        }
    }
     //Liste des employés avec leur salaire moyen par entreprise :

    public void afficherSalaireMoyenParEntreprise() {
        NodeList salaireList = document.getElementsByTagName("salaire");
        Map<String, Double> sommeSalaireParEntreprise = new HashMap<>();
        Map<String, Integer> countEmployesParEntreprise = new HashMap<>();

        for (int i = 0; i < salaireList.getLength(); i++) {
            Element salaireElement = (Element) salaireList.item(i);
            String idEntreprise = salaireElement.getAttribute("idEmploye");

            // Ajouter le montant du salaire à la somme pour l'entreprise correspondante
            double montantSalaire = Double.parseDouble(salaireElement.getElementsByTagName("Montant").item(0).getTextContent());
            sommeSalaireParEntreprise.put(idEntreprise, sommeSalaireParEntreprise.getOrDefault(idEntreprise, 0.0) + montantSalaire);

            // Compter le nombre d'employés pour chaque entreprise
            countEmployesParEntreprise.put(idEntreprise, countEmployesParEntreprise.getOrDefault(idEntreprise, 0) + 1);
        }

        // Afficher les résultats
        for (Map.Entry<String, Double> entry : sommeSalaireParEntreprise.entrySet()) {
            String idEntreprise = entry.getKey();
            double sommeSalaire = entry.getValue();
            int countEmployes = countEmployesParEntreprise.get(idEntreprise);

            // Calculer le salaire moyen
            double salaireMoyen = sommeSalaire / countEmployes;

            System.out.println("ID Entreprise: " + idEntreprise);
            System.out.println("Salaire Moyen par Employe: " + salaireMoyen);
            System.out.println(); // Saut de ligne entre chaque entreprise
        }
    }

//Dépenses totales de salaires pour l'entreprise :

//    public void calculerDepensesSalairesEntreprise() {
//        NodeList salaireList = document.getElementsByTagName("salaire");
//        double depensesTotalesSalaires = 0.0;
//
//        for (int i = 0; i < salaireList.getLength(); i++) {
//            Element salaireElement = (Element) salaireList.item(i);
//            double montantSalaire = Double.parseDouble(salaireElement.getElementsByTagName("Montant").item(0).getTextContent());
//
//            // Ajouter le montant du salaire aux dépenses totales
//            depensesTotalesSalaires += montantSalaire;
//        }
//
//        // Afficher les résultats
//        System.out.println("Dépenses totales de salaires pour l'entreprise : " + depensesTotalesSalaires);
//    }
    
    
  //Dépenses totales de salaires pour l'entreprise :


  //Dépenses totales de salaires pour l'entreprise :
  
//    public double calculerDepensesSalairesEntreprise() {
//        NodeList salaireList = document.getElementsByTagName("salaire");
//        double depensesTotalesSalaires = 0.0;
//
//        for (int i = 0; i < salaireList.getLength(); i++) {
//            Element salaireElement = (Element) salaireList.item(i);
//            double montantSalaire = Double.parseDouble(salaireElement.getElementsByTagName("Montant").item(0).getTextContent());
//
//            // Ajouter le montant du salaire aux dépenses totales
//            depensesTotalesSalaires += montantSalaire;
//        }
//
//        return depensesTotalesSalaires; // Retourner la somme des dépenses totales de salaires
//    }
    
    
 
  public double calculerDepensesSalairesEntreprise() {
      NodeList salaireList = document.getElementsByTagName("salaire");
      double depensesTotalesSalaires = 0.0;

      for (int i = 0; i < salaireList.getLength(); i++) {
          Element salaireElement = (Element) salaireList.item(i);
          // Vérifier si l'élément Montant existe avant de récupérer son contenu
          Node montantNode = salaireElement.getElementsByTagName("Montant").item(0);
          if (montantNode != null && montantNode.getNodeType() == Node.ELEMENT_NODE) {
              // Convertir le contenu de Montant en double et l'ajouter aux dépenses totales
              depensesTotalesSalaires += Double.parseDouble(montantNode.getTextContent());
          } else {
              // Gérer le cas où Montant est introuvable ou vide
              System.out.println("Attention: Montant de salaire manquant ou invalide pour l'élément " + i);
          }
      }

      return depensesTotalesSalaires; // Retourner la somme des dépenses totales de salaires
  }


//Nombre total de jours de congé par employé :

public void calculerNombreTotalJoursCongeParEmploye() {
        NodeList congeList = document.getElementsByTagName("Conge");
        Map<String, Integer> joursCongeParEmploye = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < congeList.getLength(); i++) {
            Element congeElement = (Element) congeList.item(i);
            String idEmploye = congeElement.getAttribute("idEmploye");
            String dateDebutStr = congeElement.getElementsByTagName("date_debut").item(0).getTextContent();
            String dateFinStr = congeElement.getElementsByTagName("date_fin").item(0).getTextContent();

            try {
                Date dateDebut = dateFormat.parse(dateDebutStr);
                Date dateFin = dateFormat.parse(dateFinStr);

                // Calculer la différence entre les dates
                long differenceEnMillisecondes = dateFin.getTime() - dateDebut.getTime();
                int differenceEnJours = (int) (differenceEnMillisecondes / (1000 * 60 * 60 * 24));

                // Ajouter la différence au total pour l'employé correspondant
                joursCongeParEmploye.put(idEmploye, joursCongeParEmploye.getOrDefault(idEmploye, 0) + differenceEnJours);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Afficher les résultats
        for (Map.Entry<String, Integer> entry : joursCongeParEmploye.entrySet()) {
            String idEmploye = entry.getKey();
            int totalJoursConge = entry.getValue();

            System.out.println("ID Employe: " + idEmploye);
            System.out.println("Nombre total de jours de congé: " + totalJoursConge);
            System.out.println(); // Saut de ligne entre chaque employé
        }
    }



//Liste des employés ayant pris des congés mais n'ayant pas de bonus :

public void afficherEmployesCongesSansBonus() {
        NodeList congeList = document.getElementsByTagName("Conge");
        Set<String> employesAvecConges = new HashSet<>();
        Set<String> employesAvecBonus = new HashSet<>();

        // Identifie les employés ayant pris des congés
        for (int i = 0; i < congeList.getLength(); i++) {
            Element congeElement = (Element) congeList.item(i);
            String idEmploye = congeElement.getAttribute("idEmploye");
            employesAvecConges.add(idEmploye);
        }

        // Identifie les employés ayant des bonus
        NodeList bonusList = document.getElementsByTagName("Bonus");
        for (int i = 0; i < bonusList.getLength(); i++) {
            Element bonusElement = (Element) bonusList.item(i);
            String idEmployeBonus = bonusElement.getAttribute("idEmploye");
            employesAvecBonus.add(idEmployeBonus);
        }

        // Identifie les employés ayant pris des congés mais n'ayant pas de bonus
        employesAvecConges.removeAll(employesAvecBonus);

        // Affiche les résultats
        for (String idEmployeSansBonus : employesAvecConges) {
            System.out.println("ID Employe avec congés mais sans bonus : " + idEmployeSansBonus);
        }
    }

//Liste des employés ayant pris des congés et le total des bonus qu'ils ont reçus :

public void afficherEmployesCongesAvecTotalBonus() {
        NodeList congeList = document.getElementsByTagName("Conge");
        Map<String, Integer> joursCongeParEmploye = new HashMap<>();
        Map<String, Integer> totalBonusParEmploye = new HashMap<>();

        // Calculer le total des jours de congé par employé
        for (int i = 0; i < congeList.getLength(); i++) {
            Element congeElement = (Element) congeList.item(i);
            String idEmploye = congeElement.getAttribute("idEmploye");
            int joursConge = calculerDifferenceJours(congeElement.getElementsByTagName("date_debut").item(0).getTextContent(),
                    congeElement.getElementsByTagName("date_fin").item(0).getTextContent());

            joursCongeParEmploye.put(idEmploye, joursConge);
        }

        // Calculer le total des bonus par employé
        NodeList bonusList = document.getElementsByTagName("Bonus");
        for (int i = 0; i < bonusList.getLength(); i++) {
            Element bonusElement = (Element) bonusList.item(i);
            String idEmployeBonus = bonusElement.getAttribute("idEmploye");
            int montantBonus = Integer.parseInt(bonusElement.getElementsByTagName("Montant").item(0).getTextContent());

            totalBonusParEmploye.put(idEmployeBonus, totalBonusParEmploye.getOrDefault(idEmployeBonus, 0) + montantBonus);
        }

        // Afficher les résultats
        for (Map.Entry<String, Integer> entry : joursCongeParEmploye.entrySet()) {
            String idEmploye = entry.getKey();
            int joursConge = entry.getValue();
            int totalBonus = totalBonusParEmploye.getOrDefault(idEmploye, 0);

            System.out.println("ID Employe: " + idEmploye);
            System.out.println("Nombre total de jours de congé: " + joursConge);
            System.out.println("Total des bonus reçus: " + totalBonus);
            System.out.println(); // Saut de ligne entre chaque employé
        }
    }

//Calcul de la durée moyenne de congé par employé, avec un classement :
public void calculerDureeMoyenneCongeParEmploye() {
        NodeList congeList = document.getElementsByTagName("Conge");
        Map<String, Integer> dureeTotaleCongeParEmploye = new HashMap<>();
        Map<String, Integer> nombreCongesParEmploye = new HashMap<>();

        // Calculer la durée totale des congés et le nombre de congés par employé
        for (int i = 0; i < congeList.getLength(); i++) {
            Element congeElement = (Element) congeList.item(i);
            String idEmploye = congeElement.getAttribute("idEmploye");
            int dureeConge = calculerDifferenceJours(congeElement.getElementsByTagName("date_debut").item(0).getTextContent(),
                    congeElement.getElementsByTagName("date_fin").item(0).getTextContent());

            // Mettre à jour la durée totale des congés par employé
            dureeTotaleCongeParEmploye.put(idEmploye, dureeTotaleCongeParEmploye.getOrDefault(idEmploye, 0) + dureeConge);

            // Mettre à jour le nombre de congés par employé
            nombreCongesParEmploye.put(idEmploye, nombreCongesParEmploye.getOrDefault(idEmploye, 0) + 1);
        }

        // Calculer et afficher la durée moyenne des congés par employé
        System.out.println("Classement de la durée moyenne des congés par employé :");
        for (Entry<String, Integer> entry : dureeTotaleCongeParEmploye.entrySet()) {
            String idEmploye = entry.getKey();
            int dureeTotale = entry.getValue();
            int nombreConges = nombreCongesParEmploye.get(idEmploye);
            double dureeMoyenne = (double) dureeTotale / nombreConges;

            System.out.println("ID Employe: " + idEmploye);
            System.out.println("Duree moyenne des congés: " + dureeMoyenne + " jours");
            System.out.println(); // Saut de ligne entre chaque employé
        }
    }


//Liste des employés qui ont pris des congés et des bonus dans la même période :
 public void afficherEmployesCongesEtBonusMemePeriode() {
        NodeList congeList = document.getElementsByTagName("Conge");
        NodeList bonusList = document.getElementsByTagName("Bonus");
        Map<String, List<String>> employesAvecCongesEtBonus = new HashMap<>();

        // Construire une liste des employés avec des congés
        for (int i = 0; i < congeList.getLength(); i++) {
            Element congeElement = (Element) congeList.item(i);
            String idEmployeConge = congeElement.getAttribute("idEmploye");

            employesAvecCongesEtBonus.computeIfAbsent(idEmployeConge, k -> new ArrayList<>()).add("Congé");
        }

        // Ajouter les bonus à la liste des employés avec des congés
        for (int i = 0; i < bonusList.getLength(); i++) {
            Element bonusElement = (Element) bonusList.item(i);
            String idEmployeBonus = bonusElement.getAttribute("idEmploye");

            employesAvecCongesEtBonus.computeIfAbsent(idEmployeBonus, k -> new ArrayList<>()).add("Bonus");
        }

        // Filtrer les employés avec à la fois des congés et des bonus
        List<String> employesAvecCongesEtBonusMemePeriode = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : employesAvecCongesEtBonus.entrySet()) {
            List<String> activites = entry.getValue();
            if (activites.contains("Congé") && activites.contains("Bonus")) {
                employesAvecCongesEtBonusMemePeriode.add(entry.getKey());
            }
        }

        // Afficher les résultats
        System.out.println("Employés avec à la fois des congés et des bonus dans la même période :");
        for (String idEmploye : employesAvecCongesEtBonusMemePeriode) {
            System.out.println("ID Employé : " + idEmploye);
        }
    }


//Liste des employés avec le pourcentage du salaire par rapport à la moyenne de leur entreprise :
public void afficherPourcentageSalaireParRapportMoyenneEntreprise() {
        NodeList salaireList = document.getElementsByTagName("salaire");
        Map<String, Double> sommeSalaireParEntreprise = new HashMap<>();
        Map<String, Integer> countEmployesParEntreprise = new HashMap<>();

        // Calculer la somme des salaires par entreprise
        for (int i = 0; i < salaireList.getLength(); i++) {
            Element salaireElement = (Element) salaireList.item(i);
            String idEntreprise = salaireElement.getAttribute("idEntreprise");
            double montantSalaire = Double.parseDouble(salaireElement.getElementsByTagName("Montant").item(0).getTextContent());

            sommeSalaireParEntreprise.put(idEntreprise, sommeSalaireParEntreprise.getOrDefault(idEntreprise, 0.0) + montantSalaire);
            countEmployesParEntreprise.put(idEntreprise, countEmployesParEntreprise.getOrDefault(idEntreprise, 0) + 1);
        }

        // Afficher les résultats
        System.out.println("Pourcentage du salaire par rapport à la moyenne de l'entreprise :");
        for (int i = 0; i < salaireList.getLength(); i++) {
            Element salaireElement = (Element) salaireList.item(i);
            String idEmploye = salaireElement.getAttribute("idEmploye");
            double montantSalaire = Double.parseDouble(salaireElement.getElementsByTagName("Montant").item(0).getTextContent());

            // Récupérer les informations sur l'entreprise de l'employé
            String idEntreprise = salaireElement.getAttribute("idEntreprise");
            Double sommeSalaireEntreprise = sommeSalaireParEntreprise.get(idEntreprise);
            Integer nombreEmployesEntreprise = countEmployesParEntreprise.get(idEntreprise);

            // Vérifier si les informations nécessaires sont disponibles
            if (sommeSalaireEntreprise != null && nombreEmployesEntreprise != null && nombreEmployesEntreprise != 0) {
                // Calculer la moyenne du salaire par employé
                double moyenneSalaireEntreprise = sommeSalaireEntreprise / nombreEmployesEntreprise;
                double pourcentage = (montantSalaire / moyenneSalaireEntreprise) * 100;

                System.out.println("ID Employé: " + idEmploye);
                System.out.println("Pourcentage du salaire par rapport à la moyenne de l'entreprise: " + pourcentage + "%");
                System.out.println(); // Saut de ligne entre chaque employé
            } else {
                System.out.println("Les informations nécessaires pour le calcul ne sont pas disponibles pour l'employé ID: " + idEmploye);
            }
        }
    }

//Liste des employés avec le montant total des bonus et la moyenne de salaire, en incluant uniquement ceux ayant pris des congés dans une plage spécifique de dates :
public void afficherEmployesAvecBonusEtMoyenneSalaireDansPlageDates(String dateDebutPlage, String dateFinPlage) {

        NodeList congeList = document.getElementsByTagName("Conge");
        NodeList bonusList = document.getElementsByTagName("Bonus");
        NodeList salaireList = document.getElementsByTagName("salaire");

        Set<String> employesDansPlageDates = new HashSet<>();

        // Filtrer les employés ayant pris des congés dans la plage spécifique de dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateDebut = dateFormat.parse(dateDebutPlage);
            Date dateFin = dateFormat.parse(dateFinPlage);

            for (int i = 0; i < congeList.getLength(); i++) {
                Element congeElement = (Element) congeList.item(i);
                String idEmployeConge = congeElement.getAttribute("idEmploye");
                String dateDebutCongeStr = congeElement.getElementsByTagName("date_debut").item(0).getTextContent();
                Date dateDebutConge = dateFormat.parse(dateDebutCongeStr);

                if (dateDebutConge.compareTo(dateDebut) >= 0 && dateDebutConge.compareTo(dateFin) <= 0) {
                    employesDansPlageDates.add(idEmployeConge);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return; // Gérer l'erreur de conversion de date
        }

        // Calculer le montant total des bonus et la moyenne de salaire pour les employés dans la plage de dates
        Map<String, Double> totalBonusParEmploye = new HashMap<>();
        Map<String, Double> sommeSalaireParEmploye = new HashMap<>();
        Map<String, Integer> countEmployesParEntreprise = new HashMap<>();

        for (int i = 0; i < bonusList.getLength(); i++) {
            Element bonusElement = (Element) bonusList.item(i);
            String idEmployeBonus = bonusElement.getAttribute("idEmploye");
            double montantBonus = Double.parseDouble(bonusElement.getElementsByTagName("Montant").item(0).getTextContent());

            if (employesDansPlageDates.contains(idEmployeBonus)) {
                // Ajouter le montant du bonus à la somme pour l'employé correspondant
                totalBonusParEmploye.put(idEmployeBonus, totalBonusParEmploye.getOrDefault(idEmployeBonus, 0.0) + montantBonus);
            }
        }

        for (int i = 0; i < salaireList.getLength(); i++) {
            Element salaireElement = (Element) salaireList.item(i);
            String idEmploye = salaireElement.getAttribute("idEmploye");
            double montantSalaire = Double.parseDouble(salaireElement.getElementsByTagName("Montant").item(0).getTextContent());

            if (employesDansPlageDates.contains(idEmploye)) {
                // Ajouter le montant du salaire à la somme pour l'employé correspondant
                sommeSalaireParEmploye.put(idEmploye, sommeSalaireParEmploye.getOrDefault(idEmploye, 0.0) + montantSalaire);

                // Compter le nombre d'employés pour chaque entreprise
                countEmployesParEntreprise.put(idEmploye, countEmployesParEntreprise.getOrDefault(idEmploye, 0) + 1);
            }
        }

        // Afficher les résultats
        System.out.println("Employés avec bonus et moyenne de salaire dans la plage de dates :");
        for (String idEmploye : employesDansPlageDates) {
            double totalBonus = totalBonusParEmploye.getOrDefault(idEmploye, 0.0);
            double sommeSalaire = sommeSalaireParEmploye.getOrDefault(idEmploye, 0.0);
            int nombreEmployes = countEmployesParEntreprise.getOrDefault(idEmploye, 1); // éviter la division par zéro

            // Calculer la moyenne du salaire
            double moyenneSalaire = sommeSalaire / nombreEmployes;

            System.out.println("ID Employé: " + idEmploye);
            System.out.println("Montant total des bonus : " + totalBonus);
            System.out.println("Moyenne de salaire : " + moyenneSalaire);
            System.out.println(); // Saut de ligne entre chaque employé
        }
    }

       public void calculerMontantTotalBonusParMoisAnneePrecedente() {
    NodeList bonusList = document.getElementsByTagName("Bonus");

    // Filtrer les bonus de l'année précédente
    Calendar calendar = Calendar.getInstance();
    int anneeCourante = calendar.get(Calendar.YEAR);
    calendar.set(Calendar.YEAR, anneeCourante - 1);
    Date debutAnneePrecedente = calendar.getTime();
    Date finAnneePrecedente = new Date();

    List<Element> bonusAnneePrecedente = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    for (int i = 0; i < bonusList.getLength(); i++) {
        Element bonusElement = (Element) bonusList.item(i);
        String dateBonusStr = bonusElement.getElementsByTagName("date").item(0).getTextContent();

        try {
            Date dateBonus = dateFormat.parse(dateBonusStr);

            if (dateBonus.after(debutAnneePrecedente) && dateBonus.before(finAnneePrecedente)) {
                bonusAnneePrecedente.add(bonusElement);
            }
        } catch (ParseException e) {
            System.err.println("Erreur de parsing de dateBonusStr : " + dateBonusStr);
            e.printStackTrace();
        }
    }

    // Regrouper les bonus par mois
    Map<String, Double> montantTotalParMois = new HashMap<>();
    SimpleDateFormat moisFormat = new SimpleDateFormat("MM");

    for (Element bonusElement : bonusAnneePrecedente) {
        String dateBonusStr = bonusElement.getElementsByTagName("date").item(0).getTextContent();

        try {
            Date dateBonus = dateFormat.parse(dateBonusStr);
            String moisBonusStr = moisFormat.format(dateBonus);
            double montantBonus = Double.parseDouble(bonusElement.getElementsByTagName("Montant").item(0).getTextContent());

            montantTotalParMois.merge(moisBonusStr, montantBonus, Double::sum);
        } catch (ParseException | NumberFormatException e) {
            System.err.println("Erreur de parsing lors du traitement d'un bonus");
            e.printStackTrace();
        }
    }

    // Afficher les résultats
    System.out.println("Montant total des bonus distribués chaque mois de l'année précédente :");
    montantTotalParMois.forEach((mois, montantTotal) -> {
        System.out.println("Mois : " + mois);
        System.out.println("Montant total : " + montantTotal);
        System.out.println(); // Saut de ligne entre chaque mois
    });
}


public static void main(String[] args) {
    String dateDebutPlage = "2024-01-01";
    String dateFinPlage = "2024-12-31";
        xmlDAO entrepriseDAO = new xmlDAO("ressources/xml/bdChina.xml");
        
      //  calculerDepensesSalairesEntreprise();
        entrepriseDAO.afficherSalaireMoyenParEntreprise();
    //    entrepriseDAO.calculerDepensesSalairesEntreprise();
        entrepriseDAO.calculerNombreTotalJoursCongeParEmploye();
        entrepriseDAO.afficherEmployesCongesSansBonus();
        entrepriseDAO.afficherEmployesCongesAvecTotalBonus();
        entrepriseDAO.calculerDureeMoyenneCongeParEmploye();
        entrepriseDAO.afficherEmployesCongesEtBonusMemePeriode();
        entrepriseDAO.afficherPourcentageSalaireParRapportMoyenneEntreprise();
        entrepriseDAO.afficherEmployesAvecBonusEtMoyenneSalaireDansPlageDates(dateDebutPlage, dateFinPlage);
        entrepriseDAO.calculerMontantTotalBonusParMoisAnneePrecedente();
       

}
    }



