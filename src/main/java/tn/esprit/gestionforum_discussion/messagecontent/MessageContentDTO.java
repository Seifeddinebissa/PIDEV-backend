package tn.esprit.gestionforum_discussion.messagecontent;


import lombok.Data;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageContentDTO {
    private UUID id;
    private String content;
    private LocalDateTime dateSent;
    private MessageType messageType;

    private UUID messageRoomId;
    private String sender;

    public MessageContentDTO() {
    }

    public MessageContentDTO(UUID id, String content, LocalDateTime dateSent, MessageType messageType, UUID messageRoomId, String sender) {
        this.id = id;
        this.content = content;
        this.dateSent = dateSent;
        this.messageType = messageType;
        this.messageRoomId = messageRoomId;
        this.sender = sender;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDateTime dateSent) {
        this.dateSent = dateSent;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public UUID getMessageRoomId() {
        return messageRoomId;
    }

    public void setMessageRoomId(UUID messageRoomId) {
        this.messageRoomId = messageRoomId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}