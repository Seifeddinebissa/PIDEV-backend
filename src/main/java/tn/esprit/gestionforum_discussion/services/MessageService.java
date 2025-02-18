package tn.esprit.gestionforum_discussion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionforum_discussion.entities.Forum;
import tn.esprit.gestionforum_discussion.entities.Message;
import tn.esprit.gestionforum_discussion.repositories.MessageReposiitory;

import java.util.List;

@Service
public class MessageService {
   @Autowired
    private MessageReposiitory messageReposiitory;

    public List<Message> getAllMessage(){return messageReposiitory.findAll();}
    public Message getMessageById(Long id){return messageReposiitory.findById(id).get();}
    public  Message addMessage(Message message){return messageReposiitory.save(message);}
    public Message updateMessage(Message message){return messageReposiitory.save(message);}
    public void deleteMessage(Long id){messageReposiitory.deleteById(id);}

}
