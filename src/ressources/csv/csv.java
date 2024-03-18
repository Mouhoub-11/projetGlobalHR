package ressources.csv;

import java.io.*;

//on utilise la bibliothèque java IO afin de créer 4 fichier csv distincts contenant les données de l'entreprise allemande
public class csv {

	public static void main(String[] args) {
		String entreprise = "idE,NomEntreprise,Localisation, \n" + "1,DeutscheFirma,Berlin\n";
		String employees = "IDEmploye,NomE,PrenomE,age,Tel,Poste,departement,idC,idB,idEnt\n" +
                "101,Müller,Hans,35,+49123456789,Directeur,Marketing,201,1\n" +
                "102,Schmidt,Anna,28,+49123456780,Ingénieur,Technologie,202,1\n" +
                "103,Wagner,Michael,40,+49123456781,Analyste,Finance,203,1\n" +
                "104,Schulz,Maria,33,+49123456782,Chargé de projet,Informatique,204,1\n" +
                "105,Fischer,Andreas,31,+49123456783,Technicien,R&D,205,1\n" +
                "106,Koch,Sabine,29,+49123456784,Secrétaire,Ressources Humaines,206,1\n" +
                "107,Bauer,Thomas,37,+49123456785,Manager,Ventes,207,1\n" +
                "108,Schröder,Julia,34,+49123456786,Consultant,Stratégie,208,1\n" +
                "109,Hoffmann,Alexander,36,+49123456787,Analyste,Marketing,209,1\n" +
                "110,Richter,Laura,30,+49123456788,Ingénieur,Technologie,210,1\n" +
                "111,Meyer,Max,38,+49123456789,Directeur,Finance,211,1\n" +
                "112,Schneider,Lena,32,+49123456790,Chargé de projet,Informatique,212,1\n" +
                "113,Becker,Simon,29,+49123456791,Technicien,R&D,213,1\n" +
                "114,Krüger,Julia,31,+49123456792,Manager,Ressources Humaines,214,1\n" +
                "115,Schwarz,Frank,36,+49123456793,Consultant,Stratégie,215,1\n" +
                "116,Hofmann,Lisa,33,+49123456794,Ingénieur,Technologie,216,1\n" +
                "117,Fuchs,Philipp,39,+49123456795,Directeur,Ventes,217,1\n" +
                "118,Peters,Sarah,35,+49123456796,Secrétaire,Ressources Humaines,218,1\n" +
                "119,Berger,Matthias,37,+49123456797,Technicien,R&D,219,1\n" +
                "120,Wolf,Katharina,30,+49123456798,Manager,Ventes,220,1\n";


		String conges = "IDC,DatedebC,DatefinC,RaisonC,idEmploye\n" + "401,2024-03-01,2024-03-10,Congé annuel,101\n"
				+ "402,2024-04-15,2024-04-20,Congé maladie,102\n" + "403,2024-05-01,2024-05-10,Congé annuel,103\n"
				+ "404,2024-06-01,2024-06-10,Congé paternité,104\n" + "405,2024-07-15,2024-07-25,Congé maternité,105\n"
				+ "406,2024-08-01,2024-08-10,Congé sans solde,106\n" + "407,2024-09-05,2024-09-15,Congé annuel,107\n"
				+ "408,2024-10-01,2024-10-10,Formation professionnelle,108\n"
				+ "409,2024-11-15,2024-11-25,Congé maladie,109\n" + "410,2024-12-01,2024-12-10,Congé annuel,110\n"
				+ "411,2025-01-01,2025-01-10,Congé annuel,111\n" + "412,2025-02-15,2025-02-25,Congé annuel,112\n"
				+ "413,2025-03-01,2025-03-10,Congé sans solde,113\n" + "414,2025-04-15,2025-04-20,Congé maladie,114\n"
				+ "415,2025-05-01,2025-05-10,Congé annuel,115\n" + "416,2025-06-01,2025-06-10,Congé annuel,116\n"
				+ "417,2025-07-15,2025-07-25,Congé maternité,117\n" + "418,2025-08-01,2025-08-10,Congé annuel,118\n"
				+ "419,2025-09-05,2025-09-15,Congé annuel,119\n" + "420,2025-10-01,2025-10-10,Congé annuel,120\n";

		String bonuses = "IDB,MontantB,RaisonB,DateB,idEmploye\n" + "201,1000,Performance,2024-02-01,101\n"
				+ "202,800,Innovation,2024-02-15,102\n" + "203,1200,Excellence,2024-03-10,103\n"
				+ "204,600,Anniversaire,2024-03-20,104\n" + "205,1100,Leadership,2024-04-05,105\n"
				+ "206,750,Réussite collective,2024-04-15,106\n" + "207,900,Innovation,2024-05-01,107\n"
				+ "208,950,Motivation,2024-05-15,108\n" + "209,1300,Performance exceptionnelle,2024-06-01,109\n"
				+ "210,700,Récompense spéciale,2024-06-15,110\n" + "211,850,Productivité,2024-07-01,111\n"
				+ "212,1000,Innovation,2024-07-15,112\n" + "213,1200,Excellence,2024-08-01,113\n"
				+ "214,700,Performance,2024-08-15,114\n" + "215,800,Réussite collective,2024-09-01,115\n"
				+ "216,950,Motivation,2024-09-15,116\n" + "217,1100,Leadership,2024-10-01,117\n"
				+ "218,750,Innovation,2024-10-15,118\n" + "219,900,Excellence,2024-11-01,119\n"
				+ "220,1300,Performance exceptionnelle,2024-11-15,120\n";

		String salaires = "IDS,Montant,DateVersement,MethodeCalcul,IDEmploye\n" +
		        "301,55000,2024-02-28,Salaire fixe,101\n" +
		        "302,62000,2024-02-28,Salaire fixe,102\n" +
		        "303,58000,2024-02-28,Salaire horaire,103\n" +
		        "304,49000,2024-03-05,Salaire horaire,104\n" +
		        "305,60000,2024-03-20,Salaire horaire,105\n" +
		        "306,56000,2024-04-01,Salaire fixe,106\n" +
		        "307,54000,2024-04-15,Salaire fixe,107\n" +
		        "308,57000,2024-05-01,Salaire fixe,108\n" +
		        "309,59000,2024-05-15,Salaire fixe,109\n" +
		        "310,63000,2024-06-01,Salaire fixe,110\n" +
		        "311,55000,2024-06-30,Salaire horaire,111\n" +
		        "312,62000,2024-07-31,Salaire horaire,112\n" +
		        "313,58000,2024-08-31,Salaire fixe,113\n" +
		        "314,49000,2024-09-30,Salaire fixe,114\n" +
		        "315,60000,2024-10-31,Salaire fixe,115\n" +
		        "316,56000,2024-11-30,Salaire fixe,116\n" +
		        "317,54000,2024-12-31,Salaire fixe,117\n" +
		        "318,57000,2025-01-31,Salaire fixe,118\n" +
		        "319,59000,2025-02-28,Salaire fixe,119\n" +
		        "320,63000,2025-03-31,Salaire fixe,120\n";


		try {
			// ecrire les données
			ecrireDansCSV(employees, "employees.csv");
			ecrireDansCSV(salaires, "salaires.csv");
			ecrireDansCSV(bonuses, "bonuses.csv");
			ecrireDansCSV(conges, "conges.csv");
			ecrireDansCSV(entreprise, "entreprise.csv");
			// Lire les données actuelles des fichiers CSV(utilisées pour pouvoir modifier
			// les fichiers)
			lireDansCSV("employees.csv");
			lireDansCSV("salaires.csv");
			lireDansCSV("bonuses.csv");
			lireDansCSV("conges.csv");
			lireDansCSV("entreprise.csv");

			System.out.println("Fichiers CSV créés avec succès.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// on rajoute une méthode pour lire les données (utiles pour modifier le fichier
	// et le mettre a jour
	private static String lireDansCSV(String fichier) throws IOException {
		StringBuilder data = new StringBuilder();
		// on lit chaque ligne du fichier et on les rajoute a data successivement
		try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
			String line;
			while ((line = br.readLine()) != null) {
				data.append(line).append("\n");
			}
		}
		return data.toString();
	}

	// on crée une méthode pour ecrire les données dans le fichier csv
	private static void ecrireDansCSV(String data, String fichier) throws IOException {
		try (FileWriter writer = new FileWriter(fichier); BufferedWriter bw = new BufferedWriter(writer)) {
			bw.write(data);
		}
	}
}
