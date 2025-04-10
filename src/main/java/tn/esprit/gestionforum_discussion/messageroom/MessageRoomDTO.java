package tn.esprit.gestionforum_discussion.messageroom;



import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageRoomDTO {
    private UUID id;
    private String name;
    private Boolean isGroup;
    private LocalDateTime createdDate;
    private String createdById;

    public MessageRoomDTO() {
    }

    public MessageRoomDTO(UUID id, String name, Boolean isGroup, LocalDateTime createdDate, String createdById) {
        this.id = id;
        this.name = name;
        this.isGroup = isGroup;
        this.createdDate = createdDate;
        this.createdById = createdById;
    }

}