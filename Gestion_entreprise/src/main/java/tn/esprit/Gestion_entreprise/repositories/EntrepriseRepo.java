package tn.esprit.Gestion_entreprise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Gestion_entreprise.entities.Entreprise;

public interface EntrepriseRepo extends JpaRepository<Entreprise, Long> {}

