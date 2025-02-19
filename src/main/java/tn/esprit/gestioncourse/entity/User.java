package tn.esprit.gestioncourse.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    private String nom;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cours> coursList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ScoreQuiz> scores;

    public User() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Cours> getCoursList() {
        return coursList;
    }

    public void setCoursList(List<Cours> coursList) {
        this.coursList = coursList;
    }

    public List<ScoreQuiz> getScores() {
        return scores;
    }

    public void setScores(List<ScoreQuiz> scores) {
        this.scores = scores;
    }

    public User(Long idUser, String nom, List<Cours> coursList, List<ScoreQuiz> scores) {
        this.idUser = idUser;
        this.nom = nom;
        this.coursList = coursList;
        this.scores = scores;
    }
}

