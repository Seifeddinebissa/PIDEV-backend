package tn.esprit.gestioncourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestioncourse.entity.Reponse;
import tn.esprit.gestioncourse.services.ReponseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reponses")

public class ReponseRestAPI {

    @Autowired
    private ReponseService reponseService;

    // Endpoint pour créer une réponse
    @PostMapping("/question/{questionId}")
    public ResponseEntity<Reponse> addReponseToQuestion(@PathVariable Long questionId, @RequestBody Reponse reponse) {
        Reponse createdReponse = reponseService.createReponse(questionId, reponse);
        return new ResponseEntity<>(createdReponse, HttpStatus.CREATED);
    }

    // Endpoint pour récupérer toutes les réponses
    @GetMapping("/getAll")
    public ResponseEntity<List<Reponse>> getAllReponses() {
        List<Reponse> reponses = reponseService.getAllReponses();
        return new ResponseEntity<>(reponses, HttpStatus.OK);
    }

    // Endpoint pour récupérer une réponse par ID
    @GetMapping("/{id}")
    public ResponseEntity<Reponse> getReponseById(@PathVariable Long id) {
        Optional<Reponse> reponse = reponseService.getReponseById(id);
        return reponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint pour modifier une réponse
    @PutMapping("/{id}")
    public ResponseEntity<Reponse> updateReponse(@PathVariable Long id, @RequestBody Reponse reponseDetails) {
        try {
            Reponse updatedReponse = reponseService.updateReponse(id, reponseDetails);
            return new ResponseEntity<>(updatedReponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint pour supprimer une réponse
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReponse(@PathVariable Long id) {
        reponseService.deleteReponse(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint pour récupérer les réponses d'une question
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Reponse>> getReponsesByQuestionId(@PathVariable Long questionId) {
        try {
            List<Reponse> reponses = reponseService.getReponsesByQuestionId(questionId);
            return new ResponseEntity<>(reponses, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
