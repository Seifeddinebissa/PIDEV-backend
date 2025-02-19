package tn.esprit.gestionformation.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestionformation.Entities.Formation;

public interface FormationRepository extends JpaRepository<Formation, Integer> {
}
