package tn.esprit.gestioncourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestioncourse.entity.Reponse;
import tn.esprit.gestioncourse.entity.ScoreQuiz;

import java.util.Optional;
import java.util.List;

public interface ScoreQuizRepository extends JpaRepository<ScoreQuiz,Long> {

    Optional<ScoreQuiz> findByUserIdUserAndQuizIdQuiz(Long userId, Long quizId);
    List<ScoreQuiz> findAll();
}
