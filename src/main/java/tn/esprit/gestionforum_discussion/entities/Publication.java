package tn.esprit.gestionforum_discussion.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
public class Publication {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private Date datePosted;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Comment> Comments;

    public Publication() {
    }

    public Publication(Long id, String content, Date datePosted, Set<Comment> comments) {
        this.id = id;
        this.content = content;
        this.datePosted = datePosted;
        Comments = comments;
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

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Set<Comment> getComments() {
        return Comments;
    }

    public void setComments(Set<Comment> comments) {
        Comments = comments;
    }
}
