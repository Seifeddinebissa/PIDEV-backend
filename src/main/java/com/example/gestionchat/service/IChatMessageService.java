package com.example.gestionchat.service;

import com.example.gestionchat.entity.ChatMessage;

public interface IChatMessageService {
    void saveMessage(ChatMessage message);
    // Ajoutez d'autres méthodes si nécessaire, par exemple :
    // List<ChatMessage> getMessagesByRole(String role);
    // List<ChatMessage> getMessagesByUserId(String userId);
}