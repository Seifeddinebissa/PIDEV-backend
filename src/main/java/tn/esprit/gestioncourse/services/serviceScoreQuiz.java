package tn.esprit.gestioncourse.services;

import tn.esprit.gestioncourse.entity.Question;
import tn.esprit.gestioncourse.entity.Quiz;
import tn.esprit.gestioncourse.entity.ScoreQuiz;

import java.util.List;
import java.util.Map;

public interface serviceScoreQuiz {

    public ScoreQuiz saveQuizScore(Long quizId, Long userId, Map<Long, String> userAnswers, boolean isTimedOut) ;
    public ScoreQuiz findScoreByUserAndQuiz(Long userId, Long quizId) ;
    public List<ScoreQuiz> getAllQuizScores() ;
    public int getMaxScoreForQuiz(Quiz quiz) ;
    public void deleteScoreQuiz(Long idScoreQuiz) ;
    public List<String> getDistinctQuizNames() ;
}
