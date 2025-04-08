package tn.esprit.Gestion_entreprise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Gestion_entreprise.entities.Application;

public interface ApplicationRepo extends JpaRepository<Application, Long> {
}
