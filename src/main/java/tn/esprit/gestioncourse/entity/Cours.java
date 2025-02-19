package tn.esprit.gestioncourse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCours;
    private String titre;
    private String description;
    private String image;
    private int enrollment ;

    @ElementCollection
    @CollectionTable(name = "cours_contenu", joinColumns = @JoinColumn(name = "idCours"))
    @Column(name = "contenu")
    private List<String> contenu;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)  // Clé étrangère vers User
    private User user;


    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizList;


    public Cours() {
    }

    public Long getIdCours() {
        return idCours;
    }

    public void setIdCours(Long idCours) {
        this.idCours = idCours;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(int enrollment) {
        this.enrollment = enrollment;
    }

    public List<String> getContenu() {
        return contenu;
    }

    public void setContenu(List<String> contenu) {
        this.contenu = contenu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Quiz> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
    }

    public Cours(Long idCours, String titre, String description, String image, int enrollment, List<String> contenu, User user, List<Quiz> quizList) {
        this.idCours = idCours;
        this.titre = titre;
        this.description = description;
        this.image = image;
        this.enrollment = enrollment;
        this.contenu = contenu;
        this.user = user;
        this.quizList = quizList;
    }
}


