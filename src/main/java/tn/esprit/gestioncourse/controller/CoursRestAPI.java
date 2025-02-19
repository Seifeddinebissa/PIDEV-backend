package tn.esprit.gestioncourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestioncourse.entity.Cours;
import tn.esprit.gestioncourse.services.CoursService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/cours")
public class CoursRestAPI {


        @Autowired
        private CoursService coursService;

        // 1. Ajouter un cours
        @PostMapping
        public ResponseEntity<Cours> ajouterCours(@RequestBody Cours cours) {
            Cours nouveauCours = coursService.ajouterCours(cours);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveauCours);
        }

        // 2. Récupérer un cours par ID
        @GetMapping("/{id}")
        public ResponseEntity<Cours> getCoursById(@PathVariable Long id) {
            return coursService.getCoursById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        // 3. Récupérer tous les cours
        @GetMapping
        public ResponseEntity<List<Cours>> getAllCours() {
            List<Cours> coursList = coursService.getAllCours();
            return ResponseEntity.ok(coursList);
        }

        // 4. Mettre à jour un cours
        @PutMapping("/{id}")
        public ResponseEntity<Cours> updateCours(@PathVariable Long id, @RequestBody Cours coursDetails) {
            try {
                Cours updatedCours = coursService.updateCours(id, coursDetails);
                return ResponseEntity.ok(updatedCours);
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        }

        // 5. Supprimer un cours
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
            coursService.deleteCours(id);
            return ResponseEntity.noContent().build();
        }


}
