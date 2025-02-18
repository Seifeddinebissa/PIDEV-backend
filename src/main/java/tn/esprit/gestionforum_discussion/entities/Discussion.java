package tn.esprit.gestionforum_discussion.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
public class Discussion {
    @Id
    @GeneratedValue
    private Long id;
    private Date createdAt;
    private String topic;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> Messages;

    public Discussion() {
    }

    public Set<Message> getMessages() {
        return Messages;
    }

    public void setMessages(Set<Message> messages) {
        Messages = messages;
    }

    public Discussion(Date createdAt, Long id, String topic) {
        this.createdAt = createdAt;
        this.id = id;
        this.topic = topic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
