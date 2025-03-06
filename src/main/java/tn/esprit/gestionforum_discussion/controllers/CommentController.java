package tn.esprit.gestionforum_discussion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionforum_discussion.entities.Commentaire;
import tn.esprit.gestionforum_discussion.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/get-all")
    public ResponseEntity<List<Commentaire>> getAllComment() {
        return new ResponseEntity<>(commentService.getAllComment(), HttpStatus.OK);
    }
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Commentaire> getCommentById(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.getCommentById(id), HttpStatus.OK);

    }
    @GetMapping("/get-by-idForum/{id}")
    public ResponseEntity<List<Commentaire>> getCommentsByForumId(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.getCommentsByForumId(id),HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Commentaire> addComment(@RequestBody Commentaire commentaire, @RequestParam("id") Long id) {
        return new ResponseEntity<>(commentService.addComment(commentaire,id), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<Commentaire> updateComment(@RequestBody Commentaire commentaire) {
        return new ResponseEntity<>(commentService.updateComment(commentaire), HttpStatus.OK);
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity deleteCommentById(@RequestParam("id") Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>("Comment deleted", HttpStatus.OK);
    }
}
