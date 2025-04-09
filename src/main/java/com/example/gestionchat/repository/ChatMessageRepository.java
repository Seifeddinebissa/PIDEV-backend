package com.example.gestionchat.repository;

import com.example.gestionchat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Vous pouvez ajouter des méthodes personnalisées si nécessaire, par exemple :
    // List<ChatMessage> findByRole(String role);
    // List<ChatMessage> findByUserId(String userId);
}