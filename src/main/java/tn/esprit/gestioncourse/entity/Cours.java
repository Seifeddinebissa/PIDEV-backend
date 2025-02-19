package tn.esprit.gestioncourse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
@Entity
public class Cours {
    @Id
    @GeneratedValue
    private Long id;
    private String titre;
    private String contenu;
    private String image;
    private String description;
    private String teacherName;

    public Cours(Long id, String titre, String contenu, String image, String description, String teacherName) {
        this.id = id;
        this.titre = titre;
        this.contenu = contenu;
        this.image = image;
        this.description = description;
        this.teacherName = teacherName;
    }

    public Cours() {
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
