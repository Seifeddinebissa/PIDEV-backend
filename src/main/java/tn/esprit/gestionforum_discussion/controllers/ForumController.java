package tn.esprit.gestionforum_discussion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.gestionforum_discussion.entities.Forumm;
import tn.esprit.gestionforum_discussion.repositories.ForumRepository;
import tn.esprit.gestionforum_discussion.services.ForumService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/forum")
@CrossOrigin(origins = "http://localhost:4200")


public class ForumController {

    private static final String UPLOAD_DIR = "uploads/";
    @Autowired
    private ForumService forumService;
    @Autowired
    private ForumRepository forumRepository;

    @GetMapping("/get-all")
    public ResponseEntity<List<Forumm>> getAllForum() {
        return new ResponseEntity<>(forumService.getAllForum(), HttpStatus.OK);
    }
    @GetMapping("/get-by-id")
    public ResponseEntity<Forumm> getForumById(@RequestParam("id") Long id) {
        return new ResponseEntity<>(forumService.getForumById(id), HttpStatus.OK);

    }
    @PostMapping( "/add")
    public ResponseEntity<Forumm> AddForum(@RequestParam("title") String title,
                                           @RequestParam("content") String content,
                                           @RequestParam("likes") int likes,
                                           @RequestParam("datePosted") String datePosted,
                                              @RequestParam("image") MultipartFile image) {
        try {
            Forumm forumm = new Forumm();
            forumm.setTitle(title);
            forumm.setContent(content);
            forumm.setLikes(likes);
            forumm.setDatePosted(LocalDate.parse(datePosted));
            forumm.setImage(image.getBytes());


            Forumm savedForumm = forumRepository.save(forumm);
            return ResponseEntity.ok(savedForumm);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/add1")
    public ResponseEntity<Forumm> addForum1(@RequestBody Forumm forummDTO) {
        Forumm forumm = new Forumm();
        forumm.setTitle(forummDTO.getTitle());
        forumm.setContent(forummDTO.getContent());
        forumm.setLikes(forummDTO.getLikes());
        forumm.setDatePosted(forummDTO.getDatePosted());
        forumm.setImage(forummDTO.getImage());

        return new ResponseEntity<>(forumService.addForum(forumm), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Forumm> updateForum(@RequestBody Forumm forumm) {
        return new ResponseEntity<>(forumService.updateForum(forumm), HttpStatus.OK);
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity deleteForumById(@RequestParam("id") Long id) {
        forumService.deleteForum(id);
        return new ResponseEntity<>("Forum deleted", HttpStatus.OK);
    }
    @GetMapping("/search")
    public List<Forumm> searchForums(@RequestParam String title) {
        return forumService.searchForumsByTitle(title);
    }

}
