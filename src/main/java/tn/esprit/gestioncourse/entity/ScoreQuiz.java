package tn.esprit.gestioncourse.entity;

import jakarta.persistence.*;

@Entity
public class ScoreQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_quiz")
    private Quiz quiz;


    public ScoreQuiz() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public ScoreQuiz(Long id, int score, User user, Quiz quiz) {
        this.id = id;
        this.score = score;
        this.user = user;
        this.quiz = quiz;
    }
}
