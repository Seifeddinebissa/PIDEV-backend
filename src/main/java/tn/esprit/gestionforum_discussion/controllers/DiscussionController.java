package tn.esprit.gestionforum_discussion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionforum_discussion.entities.Discussion;
import tn.esprit.gestionforum_discussion.services.DiscussionService;

import java.util.List;

@RestController
@RequestMapping("discussion")
public class DiscussionController {
    @Autowired
    private DiscussionService discussionService;
    @GetMapping("/get-all")
    public ResponseEntity<List<Discussion>> getAllDiscussion() {
        return new ResponseEntity<>(discussionService.getAllDiscussion(), HttpStatus.OK);
    }
    @GetMapping("/get-by-id")
    public ResponseEntity<Discussion> getDiscussionById(@RequestParam("id") Long id) {
        return new ResponseEntity<>(discussionService.getDiscussionById(id), HttpStatus.OK);

    }
    @PostMapping("/add")

    public ResponseEntity<Discussion> addFDiscussion(@RequestBody Discussion discussion) {
        return new ResponseEntity<>(discussionService.addDiscussion(discussion), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<Discussion> updateDiscussion(@RequestBody Discussion discussion) {
        return new ResponseEntity<>(discussionService.updateDiscussion(discussion), HttpStatus.OK);
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity deleteDiscusssionById(@RequestParam("id") Long id) {
        discussionService.deleteDiscussion(id);
        return new ResponseEntity<>("Discussion deleted", HttpStatus.OK);
    }
}
