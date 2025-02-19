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

        // 1. Ajouter un cours
    @Override
        public Cours ajouterCours(Cours cours) {
            return coursRepository.save(cours);
        }

        // 2. Récupérer un cours par son ID
    @Override
        public Optional<Cours> getCoursById(Long id) {
            return coursRepository.findById(id);
        }



        // 3. Récupérer tous les cours
        @Override
        public List<Cours> getAllCours() {
            return coursRepository.findAll();
        }

        // 4. Mettre à jour un cours
        @Override
        public Cours updateCours(Long id, Cours coursDetails) {
            return coursRepository.findById(id).map(cours -> {
                cours.setTitre(coursDetails.getTitre());
                cours.setDescription(coursDetails.getDescription());
                cours.setImage(coursDetails.getImage());
                cours.setEnrollment(coursDetails.getEnrollment());
                cours.setContenu(coursDetails.getContenu());
                return coursRepository.save(cours);
            }).orElseThrow(() -> new RuntimeException("Cours non trouvé avec ID : " + id));
        }

        // 5. Supprimer un cours
        @Override
        public void deleteCours(Long id) {
            coursRepository.deleteById(id);
        }


}