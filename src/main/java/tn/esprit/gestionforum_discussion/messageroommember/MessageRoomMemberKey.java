package tn.esprit.gestionforum_discussion.messageroommember;



import lombok.Data;
import tn.esprit.gestionforum_discussion.messageroom.MessageRoom;
import tn.esprit.gestionforum_discussion.user.User;

import java.io.Serializable;

@Data
public class MessageRoomMemberKey implements Serializable {
    private User user;
    private MessageRoom messageRoom;

    public MessageRoomMemberKey() {
    }

    public MessageRoomMemberKey(User user, MessageRoom messageRoom) {
        this.user = user;
        this.messageRoom = messageRoom;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MessageRoom getMessageRoom() {
        return messageRoom;
    }

    public void setMessageRoom(MessageRoom messageRoom) {
        this.messageRoom = messageRoom;
    }
}