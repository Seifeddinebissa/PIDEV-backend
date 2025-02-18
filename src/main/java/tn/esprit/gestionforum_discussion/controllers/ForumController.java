package tn.esprit.gestionforum_discussion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionforum_discussion.entities.Forum;
import tn.esprit.gestionforum_discussion.services.ForumService;

import java.util.List;

@RestController
@RequestMapping("forum")
public class ForumController {
    @Autowired
    private ForumService forumService;
    @GetMapping("/get-all")
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
    @PutMapping("/update")
    public ResponseEntity<Forum> updateForum(@RequestBody Forum forum) {
        return new ResponseEntity<>(forumService.updateForum(forum), HttpStatus.OK);
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity deleteForumById(@RequestParam("id") Long id) {
        forumService.deleteForum(id);
        return new ResponseEntity<>("Forum deleted", HttpStatus.OK);
    }
}
