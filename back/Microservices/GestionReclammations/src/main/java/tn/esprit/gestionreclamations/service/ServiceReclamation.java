package tn.esprit.gestionreclamations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamations.entities.Reclamation;
import tn.esprit.gestionreclamations.respository.ReclamationRepository;

import java.util.List;
import java.util.Optional;

@Service  // ✅ Rend la classe injectable par Spring
public class ServiceReclamation implements ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Override
    public Reclamation addReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    @Override
    public void deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
    }

    @Override
    public Reclamation updateReclamation(Long id, Reclamation reclamation) {
        if (reclamationRepository.existsById(id)) {
            reclamation.setId(id);  // Make sure the ID is set to the correct value
            return reclamationRepository.save(reclamation);
        }
        return null;  // Return null if the reclamation with the provided ID doesn't exist
    }

    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }


    @Override

    public Optional<Reclamation> getReclamationById(Long id) {
        return reclamationRepository.findById(id);
    }

}
