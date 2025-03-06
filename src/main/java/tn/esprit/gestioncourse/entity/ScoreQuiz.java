package tn.esprit.gestioncourse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class ScoreQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // S'assurer que cette colonne est la seule AUTO_INCREMENT
    private Long idScoreQuiz;

    private int score;

    private boolean isTimedOut;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)  // Assurer que la clé étrangère n'est pas nulle
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "idQuiz", nullable = false)  // Assurer que la clé étrangère n'est pas nulle
    @JsonIgnore
    private Quiz quiz;


    public ScoreQuiz() {
    }

    public Long getIdScoreQuiz() {
        return idScoreQuiz;
    }

    public void setIdScoreQuiz(Long idScoreQuiz) {
        this.idScoreQuiz = idScoreQuiz;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isTimedOut() {
        return isTimedOut;
    }

    public void setTimedOut(boolean timedOut) {
        isTimedOut = timedOut;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }


    public ScoreQuiz(Long idScoreQuiz, int score, boolean isTimedOut, User user, Quiz quiz) {
        this.idScoreQuiz = idScoreQuiz;
        this.score = score;
        this.isTimedOut = isTimedOut;
        this.user = user;
        this.quiz = quiz;
    }
}
