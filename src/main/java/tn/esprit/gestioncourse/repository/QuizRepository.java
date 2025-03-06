package tn.esprit.gestioncourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestioncourse.entity.Cours;
import tn.esprit.gestioncourse.entity.Quiz;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz,Long> {

    List<Quiz> findByCoursIdCours(Long idCours);
}
