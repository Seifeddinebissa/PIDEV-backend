package tn.esprit.gestionformation.Services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionformation.Entities.Formation;
import tn.esprit.gestionformation.Entities.User;
import tn.esprit.gestionformation.Repositories.FormationRepository;
import tn.esprit.gestionformation.Repositories.UserRepository;

import java.util.List;
import java.util.Set;

@Service
public class FormationService {
    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    public Formation getFormationById(int id) {
        return formationRepository.findById(id).orElse(null);
    }

    public Formation saveFormation(Formation formation) {
        return formationRepository.save(formation);
    }

    public void deleteFormation(int id) {
        formationRepository.deleteById(id);
    }

    public Formation updateFormation(int id, Formation updatedFormation) {
        return formationRepository.findById(id).map(formation -> {
            formation.setImage(updatedFormation.getImage());
            formation.setTitle(updatedFormation.getTitle());
            formation.setDescription(updatedFormation.getDescription());
            formation.setDuration(updatedFormation.getDuration());
            formation.setIs_public(updatedFormation.isIs_public());
            formation.setPrice(updatedFormation.getPrice());
            return formationRepository.save(formation);
        }).orElse(null);
    }

    public int getFeedbackCountByFormation(int id) {
        Formation formation = formationRepository.findById(id).orElse(null);
        if (formation != null && formation.getFeedbacks() != null) {
            return (int) formation.getFeedbacks().stream().filter(f -> !f.isIs_hidden()).count();
        }
        return 0;
    }

    public void addFormationToFavorites(int userId, int formationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));
        Hibernate.initialize(user.getFavoriteFormations()); // Ensure the collection is initialized
        user.getFavoriteFormations().add(formation);
        userRepository.save(user);
    }

    public void removeFormationFromFavorites(int userId, int formationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));
        Hibernate.initialize(user.getFavoriteFormations()); // Ensure the collection is initialized
        user.getFavoriteFormations().remove(formation);
        userRepository.save(user);
    }

    public Set<Formation> getFavoriteFormations(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Hibernate.initialize(user.getFavoriteFormations()); // Ensure the collection is initialized
        return user.getFavoriteFormations();
    }
}