package tn.esprit.gestioncourse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestioncourse.entity.Cours;
import tn.esprit.gestioncourse.entity.Quiz;
import tn.esprit.gestioncourse.repository.CoursRepository;
import tn.esprit.gestioncourse.repository.QuizRepository;

import java.util.List;


@Service
@Slf4j
public class QuizService implements serviceQuiz {

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private QuizRepository quizRepository;


    @Override
    public Quiz addQuizToCourse(Long idCours, Quiz quiz) {
        Cours cours = coursRepository.findById(idCours)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé avec l'ID: " + idCours));

        quiz.setCours(cours);  // Affecter le quiz au cours
        return quizRepository.save(quiz);
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Override
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz non trouvé avec l'ID: " + id));
    }

    // 📚 Trouver les Quiz par Cours
    @Override
    public List<Quiz> getQuizzesByCourse(Long idCours) {
        return quizRepository.findByCoursIdCours(idCours);
    }


    @Override
    public Quiz updateQuiz(Long idQuiz, Quiz updatedQuiz) {
        Quiz quiz = quizRepository.findById(idQuiz)
                .orElseThrow(() -> new RuntimeException("Quiz non trouvé avec l'ID: " + idQuiz));

        quiz.setTitre(updatedQuiz.getTitre());
        quiz.setDescription(updatedQuiz.getDescription());

        // Mise à jour du cours s'il est fourni
        if (updatedQuiz.getCours() != null && updatedQuiz.getCours().getIdCours() != null) {
            Cours cours = coursRepository.findById(updatedQuiz.getCours().getIdCours())
                    .orElseThrow(() -> new RuntimeException("Cours non trouvé avec l'ID: " + updatedQuiz.getCours().getIdCours()));
            quiz.setCours(cours);
        }

        return quizRepository.save(quiz);
    }


    @Override
    public void deleteQuiz(Long idQuiz) {
        Quiz quiz = quizRepository.findById(idQuiz)
                .orElseThrow(() -> new RuntimeException("Quiz non trouvé avec l'ID: " + idQuiz));

        quizRepository.delete(quiz);
    }

}
