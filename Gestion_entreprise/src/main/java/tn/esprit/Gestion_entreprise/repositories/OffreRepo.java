package tn.esprit.Gestion_entreprise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Gestion_entreprise.entities.Offre;


public interface OffreRepo extends JpaRepository<Offre, Long> {}
