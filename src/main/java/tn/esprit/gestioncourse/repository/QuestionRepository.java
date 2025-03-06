package tn.esprit.gestioncourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestioncourse.entity.Question;
import tn.esprit.gestioncourse.entity.Quiz;
import tn.esprit.gestioncourse.entity.ScoreQuiz;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {

    List<Question> findByQuiz(Quiz quiz);
}
