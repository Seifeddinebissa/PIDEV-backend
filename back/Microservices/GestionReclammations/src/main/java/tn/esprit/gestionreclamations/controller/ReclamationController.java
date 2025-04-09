package tn.esprit.gestionreclamations.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tn.esprit.gestionreclamations.entities.Reclamation;
import tn.esprit.gestionreclamations.service.ReclamationService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/reclamation")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationservice;

    @PostMapping("/add")
    public Reclamation addReclamation(@RequestBody Reclamation reclamation) {
        return reclamationservice.addReclamation(reclamation);  // ✅ Ajoute via endpoint
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReclamation(@PathVariable Long id) {
        reclamationservice.deleteReclamation(id);  // Delete reclamation by id
    }

    @PutMapping("/update/{id}")
    public Reclamation updateReclamation(@PathVariable Long id, @RequestBody Reclamation reclamation) {
        return reclamationservice.updateReclamation(id, reclamation);  // Update reclamation by ID
    }

    // Lire toutes les réclamations
    @GetMapping("/all")
    public List<Reclamation> getAllReclamations() {
        return reclamationservice.getAllReclamations();  // Récupérer toutes les réclamations
    }

    // Lire une réclamation par ID
    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> getReclamationById(@PathVariable Long id) {
        Optional<Reclamation> reclamation = reclamationservice.getReclamationById(id);
        return reclamation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



}
