package tn.esprit.gestionforum_discussion.messageroommember;



import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import tn.esprit.gestionforum_discussion.messageroom.MessageRoom;
import tn.esprit.gestionforum_discussion.messageroom.MessageRoomRepository;
import tn.esprit.gestionforum_discussion.user.User;
import tn.esprit.gestionforum_discussion.user.UserRepository;

@Component
@RequiredArgsConstructor
public class MessageRoomMemberMapper {

    private final UserRepository userRepository;
    private final MessageRoomRepository messageRoomRepository;


    public MessageRoomMemberDTO toDTO(final MessageRoomMember messageRoomMember, final MessageRoomMemberDTO messageRoomMemberDTO) {
        messageRoomMemberDTO.setMessageRoomId(messageRoomMember.getMessageRoom().getId());
        messageRoomMemberDTO.setUsername(messageRoomMember.getUser().getUsername());
        messageRoomMemberDTO.setIsAdmin(messageRoomMember.getIsAdmin());
        messageRoomMemberDTO.setLastSeen(messageRoomMember.getLastSeen());
        messageRoomMemberDTO.setLastLogin(messageRoomMember.getUser().getLastLogin());
        return messageRoomMemberDTO;
    }


    public MessageRoomMember toEntity(final MessageRoomMemberDTO messageRoomMemberDTO, final MessageRoomMember messageRoomMember) {
        final MessageRoom messageRoom = messageRoomMemberDTO.getMessageRoomId() == null ? null : messageRoomRepository.findById(messageRoomMemberDTO.getMessageRoomId())
                .orElseThrow(() -> new EntityNotFoundException("MessageRoom not found"));
        messageRoomMember.setMessageRoom(messageRoom);
        final User createdBy = messageRoomMemberDTO.getUsername() == null ? null : userRepository.findById(messageRoomMemberDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        messageRoomMember.setUser(createdBy);
        messageRoomMember.setIsAdmin(messageRoomMemberDTO.getIsAdmin());
        messageRoomMember.setLastSeen(messageRoomMemberDTO.getLastSeen());
        return messageRoomMember;
    }
}