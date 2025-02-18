package tn.esprit.gestionforum_discussion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionforum_discussion.entities.Comment;
import tn.esprit.gestionforum_discussion.entities.Forum;
import tn.esprit.gestionforum_discussion.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/get-all")
    public ResponseEntity<List<Comment>> getAllComment() {
        return new ResponseEntity<>(commentService.getAllComment(), HttpStatus.OK);
    }
    @GetMapping("/get-by-id")
    public ResponseEntity<Comment> getCommentById(@RequestParam("id") Long id) {
        return new ResponseEntity<>(commentService.getCommentById(id), HttpStatus.OK);

    }
    @PostMapping("/add")

    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        return new ResponseEntity<>(commentService.addComment(comment), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        return new ResponseEntity<>(commentService.updateComment(comment), HttpStatus.OK);
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity deleteCommentById(@RequestParam("id") Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>("Comment deleted", HttpStatus.OK);
    }
}
