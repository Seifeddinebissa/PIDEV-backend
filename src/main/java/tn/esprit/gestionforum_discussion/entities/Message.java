package tn.esprit.gestionforum_discussion.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private Date dateCreated;

    public Message() {
    }

    public Message(Long id, String content, Date dateCreated) {
        this.id = id;
        this.content = content;
        this.dateCreated = dateCreated;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
