package tn.esprit.gestioncourse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReponse;

    private String contenu;
    private boolean isCorrect;



    @Enumerated(EnumType.STRING)
    private CorrectAnswer answernum;

    @ManyToOne
    @JoinColumn(name = "idQuestion")
    @JsonIgnore
    private Question question;

    public Reponse() {
    }

    public Reponse(Long idReponse, String contenu, boolean isCorrect, CorrectAnswer answernum, Question question) {
        this.idReponse = idReponse;
        this.contenu = contenu;
        this.isCorrect = isCorrect;
        this.answernum = answernum;
        this.question = question;
    }

    public Long getIdReponse() {
        return idReponse;
    }

    public void setIdReponse(Long idReponse) {
        this.idReponse = idReponse;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public CorrectAnswer getAnswernum() {
        return answernum;
    }

    public void setAnswernum(CorrectAnswer answernum) {
        this.answernum = answernum;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
