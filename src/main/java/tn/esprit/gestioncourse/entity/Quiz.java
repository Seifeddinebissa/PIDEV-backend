package tn.esprit.gestioncourse.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuiz;

    private String titre;
    private String description;

    @ManyToOne
    @JoinColumn(name = "idCours")
    private Cours cours;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> questions;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<ScoreQuiz> scores;


    public Quiz() {
    }

    public Long getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(Long idQuiz) {
        this.idQuiz = idQuiz;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<ScoreQuiz> getScores() {
        return scores;
    }

    public void setScores(List<ScoreQuiz> scores) {
        this.scores = scores;
    }

    public Quiz(Long idQuiz, String titre, String description, Cours cours, List<Question> questions, List<ScoreQuiz> scores) {
        this.idQuiz = idQuiz;
        this.titre = titre;
        this.description = description;
        this.cours = cours;
        this.questions = questions;
        this.scores = scores;
    }
}
