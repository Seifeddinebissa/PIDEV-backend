package tn.esprit.gestionformation.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionformation.Entities.Formation;
import tn.esprit.gestionformation.Repositories.FormationRepository;

import java.util.List;

@Service
public class FormationService {
    @Autowired
    private FormationRepository formationRepository;

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
        if (formation != null) {
            return formation.getFeedbacks().size();
        }
        return 0;
    }
}