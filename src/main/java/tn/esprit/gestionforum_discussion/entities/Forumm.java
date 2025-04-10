package tn.esprit.gestionforum_discussion.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
public class Forumm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private int likes;
    @Lob
    private byte[] image;
    @Column(nullable = false, updatable = false)
    private LocalDate datePosted = LocalDate.now();
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Commentaire> commentaires;

    public Forumm() {
    }

    public Forumm(Long id, String title, String content, int likes, byte[] image, LocalDate datePosted, Set<Commentaire> commentaires) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.image = image;
        this.datePosted = datePosted;
        this.commentaires = commentaires;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LocalDate getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDate datePosted) {
        this.datePosted = datePosted;
    }


    public Set<Commentaire> getComments() {
        return commentaires;
    }

    public void setComments(Set<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }
}
