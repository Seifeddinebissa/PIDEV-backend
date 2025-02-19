package tn.esprit.gestioncourse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestioncourse.entity.Cours;
import tn.esprit.gestioncourse.repository.CoursRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CoursService implements service{

    @Autowired
    private CoursRepository coursRepository;

    @Override
    public List<Cours> findAll() {
        return coursRepository.findAll();
    }

    @Override
    public Cours ajouterCours(Cours c) {
        return coursRepository.save(c);
    }

    @Override
    public Optional<Cours> findByTitre(String titre) {
        return coursRepository.findByTitre(titre);
    }

    @Override
    public void deleteCours(Long id) {
        if (coursRepository.existsById(id)) {
            coursRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cours non trouvé");
        }
    }

    @Override
    public Cours updateCours(Long id, Cours updatedCours) {
        return coursRepository.findById(id).map(cours -> {
            cours.setTitre(updatedCours.getTitre());
            cours.setContenu(updatedCours.getContenu());
            cours.setImage(updatedCours.getImage());
            cours.setDescription(updatedCours.getDescription());
            cours.setTeacherName(updatedCours.getTeacherName());
            return coursRepository.save(cours);
        }).orElseThrow(() -> new RuntimeException("Cours non trouvé"));
    }





}