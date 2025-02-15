package tn.esprit.comment.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.sql.Date;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private String content ;
    private  int likes;
    private Date datePosted;

    public Comment() {
    }

    public Comment(Long id, String content, int likes, Date datePosted) {
        this.id = id;
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
}
