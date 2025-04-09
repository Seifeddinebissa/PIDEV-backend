package tn.esprit.gestionreclamations.service;

import tn.esprit.gestionreclamations.entities.Reclamation;

import java.util.List;
import java.util.Optional;

public interface ReclamationService {
    Reclamation addReclamation(Reclamation reclamation);  // ✅ Utilise l'entité Reclammation
    void deleteReclamation(Long id);
    Reclamation updateReclamation(Long id, Reclamation reclamation);

    // READ
    List<Reclamation> getAllReclamations();
    public Optional<Reclamation> getReclamationById(Long id);
}
