package dao;

import java.util.List;
import model.Entreprise;

public interface EntrepriseDAO {
    Entreprise findById(int id);
    List<Entreprise> findAll();
    void save(Entreprise entreprise);
    void update(Entreprise entreprise);
    void delete(Entreprise entreprise);
}
