package tn.esprit.gestioncourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestioncourse.entity.Cours;

import java.util.Optional;

public interface CoursRepository extends JpaRepository<Cours,Long> {
    Optional<Cours> findByTitre(String titre);

}
