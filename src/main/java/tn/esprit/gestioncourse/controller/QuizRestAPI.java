package tn.esprit.gestioncourse.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestioncourse.entity.Quiz;
import tn.esprit.gestioncourse.services.CoursService;
import tn.esprit.gestioncourse.services.QuizService;

import java.util.List;

@RestController
@RequestMapping("/Quiz")
public class QuizRestAPI {


    @Autowired
    private CoursService coursService;

    @Autowired
    private QuizService quizService;

    @PostMapping("/add/{idCours}")
    public ResponseEntity<Quiz> addQuiz(@PathVariable Long idCours, @RequestBody Quiz quiz) {
        Quiz newQuiz = quizService.addQuizToCourse(idCours, quiz);
        return ResponseEntity.status(HttpStatus.CREATED).body(newQuiz);
    }

    @GetMapping("/getAll")
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        Quiz quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }


    // 📚 Récupérer les Quiz d'un Cours spécifique
    @GetMapping("/byCourse/{idCours}")
    public List<Quiz> getQuizzesByCourse(@PathVariable Long idCours) {
        return quizService.getQuizzesByCourse(idCours);
    }


    @PutMapping("/update/{idQuiz}")
    public Quiz updateQuiz(@PathVariable Long idQuiz, @RequestBody Quiz quiz) {
        return quizService.updateQuiz(idQuiz, quiz);
    }

    @DeleteMapping("/delete/{idQuiz}")
    public void deleteQuiz(@PathVariable Long idQuiz) {
        quizService.deleteQuiz(idQuiz);
    }

}
