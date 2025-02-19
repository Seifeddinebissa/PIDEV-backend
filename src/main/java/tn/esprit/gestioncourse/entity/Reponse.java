package tn.esprit.gestioncourse.entity;

import jakarta.persistence.*;

@Entity
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReponse;

    private String contenu;
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "idQuestion")
    private Question question;

    public Reponse() {
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Reponse(Long idReponse, String contenu, boolean isCorrect, Question question) {
        this.idReponse = idReponse;
        this.contenu = contenu;
        this.isCorrect = isCorrect;
        this.question = question;
    }
}
