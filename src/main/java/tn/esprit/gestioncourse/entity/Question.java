package tn.esprit.gestioncourse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuestion;

    private String contenu;
    private int score;

    @Enumerated(EnumType.STRING)
    private CorrectAnswer correctAnswer;

    @ManyToOne
    @JoinColumn(name = "idQuiz")
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reponse> reponses;


    public Question() {
    }

    public Long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public CorrectAnswer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(CorrectAnswer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponse> reponses) {
        this.reponses = reponses;
    }

    public Question(Long idQuestion, String contenu, int score, CorrectAnswer correctAnswer, Quiz quiz, List<Reponse> reponses) {
        this.idQuestion = idQuestion;
        this.contenu = contenu;
        this.score = score;
        this.correctAnswer = correctAnswer;
        this.quiz = quiz;
        this.reponses = reponses;
    }
}
