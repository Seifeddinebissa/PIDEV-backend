package tn.esprit.gestionpaiementnotification.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionpaiementnotification.entities.Paiement;
import tn.esprit.gestionpaiementnotification.repositorys.PaiementRepository;
import tn.esprit.gestionpaiementnotification.services.PaiementService;
import tn.esprit.gestionpaiementnotification.services.PaiementServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/paiement")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @PostMapping
    public ResponseEntity<Paiement> addPaiement(@RequestBody Paiement paiement) {
        return new ResponseEntity<>(paiementService.addPaiement(paiement), HttpStatus.CREATED) ;
    }

    @PutMapping
    public ResponseEntity<Paiement> updatePaiement(@RequestBody Paiement paiement) {
        return new ResponseEntity<>(paiementService.updatePaiement(paiement), HttpStatus.OK) ;
    }

    @GetMapping("get-all")
    public ResponseEntity<List<Paiement>> getAllPaiements() {
        return new ResponseEntity<>(paiementService.getAllPaiements(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deletePaiement(@RequestParam("id") Long id) {
        paiementService.deletePaiement(id);
        return new ResponseEntity("Paiement supprimé avec succes", HttpStatus.NO_CONTENT) ;
    }

    @GetMapping("get-by-id")
    public ResponseEntity<Paiement> getPaiement(@RequestParam("id") Long id) {
        return new ResponseEntity<>(paiementService.getPaiement(id), HttpStatus.OK);
    }
}
