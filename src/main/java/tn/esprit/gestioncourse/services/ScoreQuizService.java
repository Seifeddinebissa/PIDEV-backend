package tn.esprit.gestioncourse.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestioncourse.entity.Question;
import tn.esprit.gestioncourse.entity.Quiz;
import tn.esprit.gestioncourse.entity.ScoreQuiz;
import tn.esprit.gestioncourse.entity.User;
import tn.esprit.gestioncourse.repository.QuestionRepository;
import tn.esprit.gestioncourse.repository.QuizRepository;
import tn.esprit.gestioncourse.repository.ScoreQuizRepository;
import tn.esprit.gestioncourse.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScoreQuizService implements serviceScoreQuiz {

    @Autowired
    private ScoreQuizRepository scoreQuizRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public ScoreQuiz saveQuizScore(Long quizId, Long userId, Map<Long, String> userAnswers, boolean isTimedOut) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz non trouvé avec l'ID: " + quizId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));
        List<Question> questions = questionRepository.findByQuiz(quiz);

        int score = calculateScore(questions, userAnswers); // Score 0 si temps écoulé

        ScoreQuiz scoreQuiz = new ScoreQuiz();
        scoreQuiz.setScore(score);
        scoreQuiz.setUser(user);
        scoreQuiz.setQuiz(quiz);
        scoreQuiz.setTimedOut(isTimedOut);

        return scoreQuizRepository.save(scoreQuiz);
    }


    private int calculateScore(List<Question> questions, Map<Long, String> userAnswers) {
        int totalScore = 0;
        System.out.println("Questions récupérées: " + questions);
        System.out.println("Réponses de l'utilisateur: " + userAnswers);

        for (Question question : questions) {
            Long questionId = question.getIdQuestion();
            String userAnswer = userAnswers.get(questionId);
            // Convertir CorrectAnswer en String
            String correctAnswer = question.getCorrectAnswer() != null ? question.getCorrectAnswer().name() : null;

            System.out.println("Question ID: " + questionId +
                    ", User Answer: '" + userAnswer + "'" +
                    ", Correct Answer: '" + correctAnswer + "'" +
                    ", Score: " + question.getScore());

            // Comparaison insensible à la casse et aux espaces
            if (userAnswer != null && correctAnswer != null &&
                    userAnswer.trim().equalsIgnoreCase(correctAnswer.trim())) {
                totalScore += question.getScore();
                System.out.println("Réponse correcte ! Ajout de " + question.getScore() + " au score total.");
            } else {
                System.out.println("Réponse incorrecte ou absente.");
            }
        }

        System.out.println("Score total calculé: " + totalScore);
        return totalScore;
    }


    @Override
    public ScoreQuiz findScoreByUserAndQuiz(Long userId, Long quizId) {
        return scoreQuizRepository.findByUserIdUserAndQuizIdQuiz(userId, quizId)
                .orElse(null);
    }

    // Nouvelle méthode pour récupérer tous les scores avec détails
    @Override
    public List<ScoreQuiz> getAllQuizScores() {
        return scoreQuizRepository.findAll();
    }


    @Override
    public int getMaxScoreForQuiz(Quiz quiz) {
        List<Question> questions = questionRepository.findByQuiz(quiz);
        return questions.stream().mapToInt(Question::getScore).sum();
    }


    @Override
    public void deleteScoreQuiz(Long idScoreQuiz) {
        if (!scoreQuizRepository.existsById(idScoreQuiz)) {
            throw new RuntimeException("ScoreQuiz avec l'ID " + idScoreQuiz + " non trouvé");
        }
        scoreQuizRepository.deleteById(idScoreQuiz);
    }

    @Override
    public List<String> getDistinctQuizNames() {
        return scoreQuizRepository.findAll()
                .stream()
                .map(scoreQuiz -> scoreQuiz.getQuiz().getTitre())
                .distinct()
                .collect(Collectors.toList());
    }
}
