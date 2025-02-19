package tn.esprit.gestioncourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.gestioncourse.entity.Cours;

import java.util.Optional;

public interface CoursRepository extends JpaRepository<Cours,Long> {

}
