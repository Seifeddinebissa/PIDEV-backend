package tn.esprit.gestionforum_discussion.entities;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Entity
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Value("1")
    private Long id;
    private String name;
    private String content;
    private int likes;
    private Date datePosted;

    @ManyToOne
    @JoinColumn(name = "forum_id", nullable = false)
    private Forumm forumm;

    public Commentaire(String name, String content, int likes, Date datePosted, Forumm forumm) {

        this.name = name;
        this.content = content;
        this.likes = likes;
        this.datePosted = datePosted;
        this.forumm = forumm;
    }

    public Commentaire() {
    }

    public Long getId() {
        return id;
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

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }


    public Forumm getForum() {
        return forumm;
    }

    public void setForum(Forumm forumm) {
        this.forumm = forumm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
