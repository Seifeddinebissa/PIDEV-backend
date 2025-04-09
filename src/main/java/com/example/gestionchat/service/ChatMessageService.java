package com.example.gestionchat.service;

import com.example.gestionchat.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.gestionchat.repository.ChatMessageRepository;
import java.util.List;

@Service
public class ChatMessageService implements IChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Override
    public void saveMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    // Exemple de méthode pour récupérer les messages par rôle
    public List<ChatMessage> getMessagesByRole(String role) {
        return chatMessageRepository.findAll().stream()
                .filter(msg -> msg.getRole().equals(role))
                .toList();
    }

    // Exemple de méthode pour récupérer les messages par userId
    public List<ChatMessage> getMessagesByUserId(String userId) {
        return chatMessageRepository.findAll().stream()
                .filter(msg -> msg.getUserId().equals(userId))
                .toList();
    }
}