package tn.esprit.gestioncourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestioncourse.entity.Question;
import tn.esprit.gestioncourse.repository.QuestionRepository;
import tn.esprit.gestioncourse.repository.QuizRepository;
import tn.esprit.gestioncourse.services.serviceQuestion;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService implements serviceQuestion {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;


    // Get all questions
    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // Get question by ID
    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    // Get questions by quiz ID
    @Override
    public List<Question> getQuestionsByQuizId(Long quizId) {
        return quizRepository.findById(quizId)
                .map(questionRepository::findByQuiz)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    // Update a question
    @Override
    public Question updateQuestion(Long id, Question questionDetails) {
        return questionRepository.findById(id).map(question -> {
            question.setContenu(questionDetails.getContenu());
            question.setScore(questionDetails.getScore());
            question.setCorrectAnswer(questionDetails.getCorrectAnswer());
            question.setQuiz(questionDetails.getQuiz());
            question.setReponses(questionDetails.getReponses());
            return questionRepository.save(question);
        }).orElseThrow(() -> new RuntimeException("Question not found"));
    }

    // Delete a question
    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    // Add a question to a quiz
    @Override
    public Question addQuestionToQuiz(Long quizId, Question question) {
        return quizRepository.findById(quizId).map(quiz -> {
            question.setQuiz(quiz);
            return questionRepository.save(question);
        }).orElseThrow(() -> new RuntimeException("Quiz not found"));
    }
}
