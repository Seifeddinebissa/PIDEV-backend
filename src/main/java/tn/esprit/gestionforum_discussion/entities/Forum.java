package tn.esprit.gestionforum_discussion.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
public class Forum {
    @Id
    @GeneratedValue
    private Long id;
    private  String title;
    private String content;
    private int likes;
    private Date datePosted;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Publication> Publications;

    public Forum() {
    }



    public Forum(Long id, String title, String content, int likes, Date datePosted, Set<Publication> publications) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.datePosted = datePosted;

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

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Set<Publication> getPublications() {
        return Publications;
    }

    public void setPublications(Set<Publication> publications) {
        Publications = publications;
    }
}
