package tn.esprit.gestioncourse.services;

import tn.esprit.gestioncourse.entity.Cours;

import java.util.List;
import java.util.Optional;

public interface service {
    List<Cours> findAll();
    Cours ajouterCours(Cours c);
    void deleteCours(Long id);
    Cours updateCours(Long id, Cours updatedCours);
    Optional<Cours> findByTitre(String titre);
}
