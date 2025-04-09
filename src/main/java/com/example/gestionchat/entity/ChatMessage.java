package com.example.gestionchat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;  // Nom d'utilisateur ou pseudonyme
    private String userId;  // ID unique de l'utilisateur (par exemple, UUID ou format personnalisé)
    private String role;    // Rôle de l'utilisateur : "ETUDIANT" ou "ADMINISTRATION"
    private String content;
    private String type;    // Par exemple, "CHAT", "JOIN", "LEAVE"

    public ChatMessage() {
    }

    public ChatMessage(String sender, String userId, String role, String content, String type) {
        this.sender = sender;
        this.userId = userId;
        this.role = role;
        this.content = content;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}