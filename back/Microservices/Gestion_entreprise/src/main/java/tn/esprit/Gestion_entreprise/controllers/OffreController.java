package tn.esprit.Gestion_entreprise.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.Gestion_entreprise.entities.Application;
import tn.esprit.Gestion_entreprise.entities.Offre;
import tn.esprit.Gestion_entreprise.services.OffreService;
import tn.esprit.Gestion_entreprise.repositories.ApplicationRepo;

import java.util.List;

@RestController
@RequestMapping("/offres")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OffreController {

    private final OffreService offreService;
    private final ApplicationRepo applicationRepo;

    @PostMapping
    public ResponseEntity<Offre> createOffre(@RequestBody Offre offre) {
        try {
            return ResponseEntity.ok(offreService.addOffre(offre));
        } catch (Exception e) {
            e.printStackTrace(); // Or use a logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Or return a custom error message
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Offre> updateOffre(@PathVariable Long id, @RequestBody Offre offre) {
        return ResponseEntity.ok(offreService.updateOffre(id, offre));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOffre(@PathVariable Long id) {
        boolean isDeleted = offreService.deleteOffre(id);
        return isDeleted
                ? ResponseEntity.ok("Offre deleted successfully")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offre not found");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offre> getOffreById(@PathVariable Long id) {
        return ResponseEntity.ok(offreService.getOffreById(id));
    }
    @GetMapping("/entreprise/{entrepriseId}")
    public ResponseEntity<List<Offre>> getOffresByEntrepriseId(@PathVariable Long entrepriseId) {
        List<Offre> offres = offreService.getOffresByEntrepriseId(entrepriseId);
        return ResponseEntity.ok(offres);
    }

    @GetMapping
    public ResponseEntity<List<Offre>> getAllOffres() {
        return ResponseEntity.ok(offreService.getAllOffres());
    }

    @PostMapping("/apply")
    public ResponseEntity<String> applyToOffer(@RequestBody ApplicationRequest request) {
        boolean success = offreService.apply(request.getStudentId(), request.getOfferId());
        return success
                ? ResponseEntity.ok("Application submitted successfully")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Application failed");
    }

    @PostMapping("/favorites/add")
    public ResponseEntity<String> addFavoris(@RequestBody FavoriteRequest request) {
        boolean success = offreService.addFavoris(request.getStudentId(), request.getOfferId());
        return success
                ? ResponseEntity.ok("Added to favorites successfully")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already in favorites");
    }

    @PostMapping("/upload-cv")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("studentId") Long studentId,
            @RequestParam("offerId") Long offerId
    ) {
        String filePath = offreService.uploadFile(file, studentId, offerId);
        return ResponseEntity.ok("File uploaded successfully: " + filePath);
    }
    @PostMapping("/favorites/remove")
    public ResponseEntity<String> removeFavorite(@RequestBody FavoriteRequest request) {
        if (request.getStudentId() == null || request.getOfferId() == null) {
            return ResponseEntity.badRequest().body("Invalid request parameters");
        }

        boolean success = offreService.removeFavorite(request.getStudentId(), request.getOfferId());
        if (success) {
            return ResponseEntity.ok("Favorite removed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorite not found or already removed");
        }
    }



    @GetMapping("/favorites")
    public ResponseEntity<List<Offre>> getFavoriteOffers(@RequestParam Long studentId) {
        List<Offre> favorites = offreService.getFavoriteOffers(studentId);
        return ResponseEntity.ok(favorites);
    }
    @GetMapping("/applications")
    public List<Application> getApplications(@RequestParam Long studentId) {
        return offreService.getApplicationsByStudentId(studentId);
    }
    @GetMapping("/favorites/analytics")
    public ResponseEntity<List<OffreService.FavoriteStats>> getFavoriteAnalytics(
            @RequestParam(value = "limit", defaultValue = "5") int limit
    ) {
        return ResponseEntity.ok(offreService.getFavoriteAnalytics(limit));
    }
    @PutMapping("/applications/{id}/status")
    public ResponseEntity<String> updateApplicationStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        try {
            offreService.updateApplicationStatus(id, request.getStatus());
            return ResponseEntity.ok("Application status updated successfully");
        } catch (Exception e) {
            System.err.println("Error updating application status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating application");
        }
    }

    static class StatusRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    @DeleteMapping("/applications/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        try {
            applicationRepo.deleteById(id); // ✅ use the instance you autowired
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // Optional: for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete application");
        }
    }



}


