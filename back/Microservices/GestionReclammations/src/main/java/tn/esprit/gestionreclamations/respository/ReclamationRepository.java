package tn.esprit.gestionreclamations.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestionreclamations.entities.Reclamation;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {  // ✅ Nom sans conflit
}
