package ressources.xml; 

import javax.xml.parsers.*;

import org.w3c.dom.*;

public class dom {
	public static void main(String[] args) {
		try {
			// Chargement du fichier XML
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("bdChina.xml");

			// Parcours des données des employés
			NodeList employeeList = doc.getElementsByTagName("Employe");
			for (int temp = 0; temp < employeeList.getLength(); temp++) {
				Node employeeNode = employeeList.item(temp);
				if (employeeNode.getNodeType() == Node.ELEMENT_NODE) {
					Element employeeElement = (Element) employeeNode;
					String idEmploye = employeeElement.getAttribute("idEmploye");
					String nom = employeeElement.getElementsByTagName("nomE").item(0).getTextContent();
					String prenom = employeeElement.getElementsByTagName("prenomE").item(0).getTextContent();
					String age = employeeElement.getElementsByTagName("age").item(0).getTextContent();
					String telephone = employeeElement.getElementsByTagName("Telephone").item(0).getTextContent();

					// Affichage des données de l'employé
					System.out.println("ID Employe: " + idEmploye);
					System.out.println("Nom: " + nom);
					System.out.println("Prenom: " + prenom);
					System.out.println("Age: " + age);
					System.out.println("Telephone: " + telephone);

					// Recherche des salaires associées à cet employé
					NodeList salaireList = doc.getElementsByTagName("salaire");
					for (int i = 0; i < salaireList.getLength(); i++) {
						Element salaire = (Element) salaireList.item(i);
						if (salaire.getAttribute("idEmploye").equals(idEmploye)) {
							String montant = salaire.getElementsByTagName("Montant").item(0).getTextContent();
							String dateVersement = salaire.getElementsByTagName("dateVersement").item(0)
									.getTextContent();
							System.out
									.println("salaire - Montant: " + montant + ", Date de versement: " + dateVersement);
						}
					}

					// Vérification de la présence de bonus pour cet employé
					NodeList bonusList = doc.getElementsByTagName("Bonus");
					boolean bonusFound = false;
					for (int i = 0; i < bonusList.getLength(); i++) {
						Element bonusElement = (Element) bonusList.item(i);
						if (bonusElement.getAttribute("idEmploye").equals(idEmploye)) {
							String montant = bonusElement.getElementsByTagName("Montant").item(0).getTextContent();
							String raison = bonusElement.getElementsByTagName("raison").item(0).getTextContent();
							String date = bonusElement.getElementsByTagName("date").item(0).getTextContent();
							System.out.println("Bonus: " + raison + " de " + montant + " le " + date);
							bonusFound = true;
						}
					}
					if (!bonusFound) {
						System.out.println("Aucun bonus associe a cet employe.");
					}

					// Vérification de la présence de congés pour cet employé
					NodeList congeList = doc.getElementsByTagName("Conge");
					boolean congeFound = false;
					for (int i = 0; i < congeList.getLength(); i++) {
						Element congeElement = (Element) congeList.item(i);
						if (congeElement.getAttribute("idEmploye").equals(idEmploye)) {
							String dateDebut = congeElement.getElementsByTagName("date_debut").item(0).getTextContent();
							String dateFin = congeElement.getElementsByTagName("date_fin").item(0).getTextContent();
							String raisonC = congeElement.getElementsByTagName("raisonC").item(0).getTextContent();
							System.out.println("Conge: " + raisonC + " du " + dateDebut + " au " + dateFin);
							congeFound = true;
						}
					}
					if (!congeFound) {
						System.out.println("Aucun conge associe a cet employe.");
					}

					System.out.println(); // Saut de ligne entre chaque employé
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}