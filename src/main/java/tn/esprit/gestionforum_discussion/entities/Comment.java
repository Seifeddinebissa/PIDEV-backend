package tn.esprit.gestionforum_discussion.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private int likes;
    private Date datePosted;
}
