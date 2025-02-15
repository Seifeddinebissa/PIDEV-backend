package tn.esprit.gestionforum.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Forum {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private int likes;

    private Date datePosted;

    public Forum() {
    }

    public Forum(long id, String title, int likes, Date datePosted) {
        this.id = id;
        this.title = title;
        this.likes = likes;
        this.datePosted = datePosted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
