package tn.esprit.gestionforum_discussion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionforum_discussion.entities.Forum;
import tn.esprit.gestionforum_discussion.entities.Publication;
import tn.esprit.gestionforum_discussion.services.PublicationService;

import java.util.List;

@RestController
@RequestMapping("publication")
public class PublicationController {
    @Autowired
    private PublicationService publicationService;
    @GetMapping("/get-all")
    public ResponseEntity<List<Publication>> getAllPublications() {
        return new ResponseEntity<>(publicationService.getAllPublication(), HttpStatus.OK);
    }
    @GetMapping("/get-by-id")
    public ResponseEntity<Publication> getPublicationById(@RequestParam("id") Long id) {
        return new ResponseEntity<>(publicationService.getPublicationById(id), HttpStatus.OK);

    }
    @PostMapping("/add")

    public ResponseEntity<Publication> addPublication(@RequestBody Publication publication) {
        return new ResponseEntity<>(publicationService.addPublication(publication), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<Publication> updatePublication(@RequestBody Publication publication) {
        return new ResponseEntity<>(publicationService.updatePublication(publication), HttpStatus.OK);
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity deletePublicationById(@RequestParam("id") Long id) {
        publicationService.deletePublication(id);
        return new ResponseEntity<>("Publication deleted", HttpStatus.OK);
    }

}
