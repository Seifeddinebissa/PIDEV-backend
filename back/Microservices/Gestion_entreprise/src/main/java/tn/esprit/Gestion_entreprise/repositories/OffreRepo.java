package tn.esprit.Gestion_entreprise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Gestion_entreprise.entities.Offre;

import java.util.List;


public interface OffreRepo extends JpaRepository<Offre, Long> {
    List<Offre> findByEntrepriseId(Long entrepriseId);
}
