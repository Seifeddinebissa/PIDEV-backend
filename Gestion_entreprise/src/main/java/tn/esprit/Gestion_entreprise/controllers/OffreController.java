package tn.esprit.Gestion_entreprise.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Gestion_entreprise.entities.Offre;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
@RequiredArgsConstructor
public class OffreController {

    @Autowired
    private tn.esprit.Gestion_entreprise.services.OffreService OffreService;

    @PostMapping
    public ResponseEntity<Offre> createOffre(@RequestBody Offre offre) {
        return ResponseEntity.ok(OffreService.addOffre(offre));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Offre> updateOffre(@PathVariable Long id, @RequestBody Offre offre) {
        return ResponseEntity.ok(OffreService.updateOffre(id, offre));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOffre(@PathVariable Long id) {
        boolean isDeleted = OffreService.deleteOffre(id);
        if (isDeleted) {
            return ResponseEntity.ok("Offre deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offre not found");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Offre> getOffreById(@PathVariable Long id) {
        return ResponseEntity.ok(OffreService.getOffreById(id));
    }

    @GetMapping
    public ResponseEntity<List<Offre>> getAllOffres() {
        return ResponseEntity.ok(OffreService.getAllOffres());
    }
}
