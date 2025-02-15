package tn.esprit.gestionforum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionforum.services.ForumService;
import tn.esprit.gestionforum.entities.Forum;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/forum")
public class ForumController {
    @Autowired
    private ForumService forumService;

    @GetMapping ("/get-all")
        public ResponseEntity<List<Forum>> getAllForum() {
        return new ResponseEntity<>(forumService.getAllForum(), HttpStatus.OK);
        }
        @GetMapping("/get-by-id")
    public ResponseEntity<Forum> getForumById(@RequestParam("id") Long id) {
        return new ResponseEntity<>(forumService.getForumById(id), HttpStatus.OK);
        }
        @PostMapping("/add")
    public ResponseEntity<Forum> addForum(@RequestBody Forum forum) {
        return new ResponseEntity<>(forumService.addForum(forum), HttpStatus.CREATED);
        }
        @PutMapping("/put")
    public ResponseEntity<Forum> putForum(@RequestBody Forum forum) {
        return new ResponseEntity<>(forumService.updateForum(forum), HttpStatus.OK);
        }
        @DeleteMapping("/delete-by-id")
    public ResponseEntity deleteForumById(@RequestParam("id") Long id) {
            forumService.deleteForum(id);
            return new ResponseEntity<>("forum deleted", HttpStatus.OK);
        }



}
