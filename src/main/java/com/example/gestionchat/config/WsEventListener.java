package com.example.gestionchat.config;

import com.example.gestionchat.controller.WsChatMessage;
import com.example.gestionchat.controller.WsChatMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WsEventListener {

    private final SimpMessageSendingOperations messageSendingOperations;
    private static final Logger log = LoggerFactory.getLogger(WsEventListener.class);

    public WsEventListener(SimpMessageSendingOperations messageSendingOperations) {
        this.messageSendingOperations = messageSendingOperations;
    }

    @EventListener
    public void handleWsDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            String role = (String) headerAccessor.getSessionAttributes().get("role");
            log.info("User disconnected: {} (Role: {})", username, role);
            var message = WsChatMessage.builder()
                    .type(WsChatMessageType.LEAVE)
                    .sender(username)
                    .role(role)
                    .build();
            messageSendingOperations.convertAndSend("/topic/public", message);
        }
    }
}