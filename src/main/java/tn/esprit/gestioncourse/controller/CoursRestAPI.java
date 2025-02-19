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


    @PostMapping("/ajouterCours")
    public Cours ajouterCours(@RequestBody Cours c){
        return coursService.ajouterCours(c);
    }


    // Mettre à jour un cours
    @PutMapping("/update/{id}")
    public ResponseEntity<Cours> updateCours(@PathVariable Long id, @RequestBody Cours updatedCours) {
        try {
            return new ResponseEntity<>(coursService.updateCours(id, updatedCours), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        try {
            coursService.deleteCours(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/titre/{titre}")
    public ResponseEntity<Cours> getCoursByTitre(@PathVariable String titre) {
        Optional<Cours> cours = coursService.findByTitre(titre);
        return cours.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }




    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cours>> listCours(){
        return new ResponseEntity<>(coursService.findAll(),
                HttpStatus.OK);

    }
}
