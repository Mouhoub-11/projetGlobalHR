package dao;

import java.util.List;
import model.Entreprise;

public interface EntrepriseDAO {
    Entreprise findById(int id);
    List<Entreprise> findAll();
    void save(Entreprise entreprise);
    void update(Entreprise entreprise);
    void delete(Entreprise entreprise);
    
    
    double getTotalSalaryExpenses(); // Calcul des dépenses totales de salaires pour l'entreprise

    List<Double> getAverageSalaryByCompany(); // Liste des salaires moyens par entreprise

    int getTotalVacationDaysByEmployee(int idEmploye); // Nombre total de jours de congé par employé

    List<Entreprise> getEmployeesWithNoBonus(); // Liste des employés n'ayant pas de bonus

    // Ajoutez d'autres méthodes pour répondre aux autres requêtes...

}




