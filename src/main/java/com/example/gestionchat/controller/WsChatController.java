package com.example.gestionchat.controller;

import com.example.gestionchat.entity.ChatMessage;
import com.example.gestionchat.service.IChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import java.util.UUID;

@Controller
public class WsChatController {

    @Autowired
    private IChatMessageService chatMessageService;  // Utiliser le service, pas le repository

    @MessageMapping("chat.sendMessage")
    @SendTo("/topic/public")
    public WsChatMessage sendMessage(@Payload WsChatMessage msg, SimpMessageHeaderAccessor headerAccessor) {
        String username = msg.getSender();
        String role = msg.getRole(); // Récupérer le rôle depuis le message
        System.out.println("Message received from " + username + " (Role: " + role + "): " + msg.getContent());

        // Générer un ID unique avec préfixe basé sur le rôle
        String userId = generateUserId(role, username);

        // Stocker l'ID et le rôle dans la session WebSocket
        headerAccessor.getSessionAttributes().put("userId", userId);
        headerAccessor.getSessionAttributes().put("role", role);

        // Sauvegarder le message dans la base de données avec l'ID et le rôle
        ChatMessage chatEntity = new ChatMessage(username, userId, role, msg.getContent(), msg.getType().name());
        chatMessageService.saveMessage(chatEntity);

        return msg;
    }

    @MessageMapping("chat.addUser")
    @SendTo("/topic/chat")
    public WsChatMessage addUser(@Payload WsChatMessage msg, SimpMessageHeaderAccessor headerAccessor) {
        String username = msg.getSender();
        String role = msg.getRole(); // Récupérer le rôle depuis le message
        System.out.println("User joined: " + username + " (Role: " + role + ")");

        // Générer un ID unique avec préfixe basé sur le rôle
        String userId = generateUserId(role, username);

        // Stocker l'ID et le rôle dans la session WebSocket
        headerAccessor.getSessionAttributes().put("userId", userId);
        headerAccessor.getSessionAttributes().put("role", role);
        headerAccessor.getSessionAttributes().put("username", username);

        // Sauvegarder l'événement de connexion avec l'ID et le rôle
        ChatMessage chatEntity = new ChatMessage(username, userId, role, "joined the chat", msg.getType().name());
        chatMessageService.saveMessage(chatEntity);

        return msg;
    }

    @MessageMapping("chat.leave")
    @SendTo("/topic/public")
    public WsChatMessage leaveUser(@Payload WsChatMessage msg, SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        String role = (String) headerAccessor.getSessionAttributes().get("role");
        if (username != null && userId != null) {
            System.out.println("User left: " + username + " (Role: " + role + ")");

            // Sauvegarder l'événement de déconnexion avec l'ID et le rôle
            ChatMessage chatEntity = new ChatMessage(username, userId, role, "left the chat", WsChatMessageType.LEAVE.name());
            chatMessageService.saveMessage(chatEntity);
        }
        return msg;
    }

    private String generateUserId(String role, String username) {
        String prefix = role.equals("ETUDIANT") ? "ETU_" : "ADM_";
        return prefix + UUID.randomUUID().toString().substring(0, 8); // Exemple : ETU_550e8400 ou ADM_550e8400
    }
}