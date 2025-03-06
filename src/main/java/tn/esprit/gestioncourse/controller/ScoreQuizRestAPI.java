package tn.esprit.gestioncourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestioncourse.entity.ScoreQuiz;
import tn.esprit.gestioncourse.services.ScoreQuizService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scoreQuiz")
public class ScoreQuizRestAPI {

    @Autowired
    private ScoreQuizService scoreQuizService;


    @PostMapping("/submit/{quizId}/{userId}")
    public ResponseEntity<ScoreQuiz> submitQuiz(
            @PathVariable Long quizId,
            @PathVariable Long userId,
            @RequestBody Map<Long, String> userAnswers,
            @RequestParam(defaultValue = "false") boolean isTimedOut
    ) {
        try {
            System.out.println("Reçu: quizId=" + quizId + ", userId=" + userId + ", userAnswers=" + userAnswers + ", isTimedOut=" + isTimedOut);
            ScoreQuiz scoreQuiz = scoreQuizService.saveQuizScore(quizId, userId, userAnswers, isTimedOut);
            System.out.println("ScoreQuiz renvoyé: " + scoreQuiz);
            return new ResponseEntity<>(scoreQuiz, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Erreur lors de la soumission du quiz: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/user/{userId}/quiz/{quizId}")
    public ResponseEntity<ScoreQuiz> getScoreForUserAndQuiz(
            @PathVariable Long userId,
            @PathVariable Long quizId
    ) {
        ScoreQuiz scoreQuiz = scoreQuizService.findScoreByUserAndQuiz(userId, quizId);
        if (scoreQuiz != null) {
            return new ResponseEntity<>(scoreQuiz, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllQuizScores() {
        List<ScoreQuiz> scores = scoreQuizService.getAllQuizScores();
        if (scores.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<Map<String, Object>> result = scores.stream().map(scoreQuiz -> {
            Map<String, Object> scoreData = new HashMap<>();
            scoreData.put("idScoreQuiz", scoreQuiz.getIdScoreQuiz());
            scoreData.put("quizName", scoreQuiz.getQuiz().getTitre());
            scoreData.put("userName", scoreQuiz.getUser().getNom());
            scoreData.put("score", scoreQuiz.getScore());
            int maxScore = scoreQuizService.getMaxScoreForQuiz(scoreQuiz.getQuiz());
            scoreData.put("maxScore", maxScore);
            scoreData.put("result", scoreQuiz.getScore() > (maxScore / 2) ? "Pass" : "Fail");
            return scoreData;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{idScoreQuiz}")
    public ResponseEntity<Void> deleteScoreQuiz(@PathVariable Long idScoreQuiz) {
        try {
            scoreQuizService.deleteScoreQuiz(idScoreQuiz);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            System.err.println("Erreur lors de la suppression du ScoreQuiz: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/quizNames")
    public ResponseEntity<List<String>> getDistinctQuizNames() {
        List<String> quizNames = scoreQuizService.getDistinctQuizNames();
        if (quizNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(quizNames, HttpStatus.OK);
    }
}