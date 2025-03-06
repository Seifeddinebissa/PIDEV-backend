package tn.esprit.gestioncourse.services;

import tn.esprit.gestioncourse.entity.Quiz;

import java.util.List;

public interface serviceQuiz {

    public Quiz addQuizToCourse(Long idCours, Quiz quiz) ;
    public List<Quiz> getAllQuizzes();
    public Quiz getQuizById(Long id) ;
    public List<Quiz> getQuizzesByCourse(Long idCours) ;
    public Quiz updateQuiz(Long idQuiz, Quiz updatedQuiz) ;
    public void deleteQuiz(Long idQuiz) ;
}
