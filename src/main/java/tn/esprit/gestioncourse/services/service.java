package tn.esprit.gestioncourse.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.gestioncourse.entity.Cours;
import tn.esprit.gestioncourse.entity.Quiz;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface service {
    public Cours ajouterCours(Cours cours) ;
    public Optional<Cours> getCoursById(Long id) ;
    public List<Cours> getAllCours() ;
    Cours updateCours(Cours cours);
    public void deleteCours(Long id) ;
    public Map<String, String> upload(MultipartFile image) ;
    public Optional<Cours> findByTitre(String titre) ;



}
