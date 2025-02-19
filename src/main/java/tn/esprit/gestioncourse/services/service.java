package tn.esprit.gestioncourse.services;

import tn.esprit.gestioncourse.entity.Cours;

import java.util.List;
import java.util.Optional;

public interface service {
    public Cours ajouterCours(Cours cours) ;
    public Optional<Cours> getCoursById(Long id) ;
    public List<Cours> getAllCours() ;
    public Cours updateCours(Long id, Cours coursDetails) ;
    public void deleteCours(Long id) ;
}
