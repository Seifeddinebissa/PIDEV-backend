package tn.esprit.gestioncourse.services;

import tn.esprit.gestioncourse.entity.Question;

import java.util.List;
import java.util.Optional;

public interface serviceQuestion {

    public List<Question> getAllQuestions() ;
    public Optional<Question> getQuestionById(Long id);
    public List<Question> getQuestionsByQuizId(Long quizId) ;
    public Question updateQuestion(Long id, Question questionDetails) ;
    public void deleteQuestion(Long id) ;
    public Question addQuestionToQuiz(Long quizId, Question question) ;
}
