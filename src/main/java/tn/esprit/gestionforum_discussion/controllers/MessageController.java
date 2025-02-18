package tn.esprit.gestionforum_discussion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionforum_discussion.entities.Message;
import tn.esprit.gestionforum_discussion.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/get-all")
    public ResponseEntity<List<Message>> getAllMessage() {
        return ResponseEntity.ok(messageService.getAllMessage());
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<Message> getMessageById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Message> addMessage(@RequestBody Message message) {
        return new ResponseEntity<>(messageService.addMessage(message), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> updateMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.updateMessage(message));
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<String> deleteMessageById(@RequestParam("id") Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok("Message deleted");
    }
}
