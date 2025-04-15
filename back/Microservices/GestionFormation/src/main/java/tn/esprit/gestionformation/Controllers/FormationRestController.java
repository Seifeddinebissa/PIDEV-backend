package tn.esprit.gestionformation.Controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.gestionformation.Entities.Feedback;
import tn.esprit.gestionformation.Entities.Formation;
import tn.esprit.gestionformation.Services.FormationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/formations")
@CrossOrigin(origins = "http://localhost:4200")
public class FormationRestController {

    private static final String UPLOAD_DIR = "/Users/mohamedbennari/Documents/uploads/";

    @Autowired
    private FormationService formationService;

    @GetMapping("/get-all")
    public List<Formation> getAllFormations() {
        return formationService.getAllFormations();
    }

    @GetMapping("/{id}")
    public Formation getFormationById(@PathVariable int id) {
        return formationService.getFormationById(id);
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Formation> createFormation(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("duration") int duration,
            @RequestParam("is_public") boolean isPublic,
            @RequestParam("price") double price,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Formation formation = new Formation();
        formation.setTitle(title);
        formation.setDescription(description);
        formation.setDuration(duration);
        formation.setIs_public(isPublic);
        formation.setPrice(price);

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            formation.setImage(imageUrl);
        }

        Formation savedFormation = formationService.saveFormation(formation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFormation);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Formation> updateFormation(
            @PathVariable int id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("duration") int duration,
            @RequestParam("is_public") boolean isPublic,
            @RequestParam("price") double price,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Formation formation = formationService.getFormationById(id);
        if (formation == null) {
            return ResponseEntity.notFound().build();
        }

        formation.setTitle(title);
        formation.setDescription(description);
        formation.setDuration(duration);
        formation.setIs_public(isPublic);
        formation.setPrice(price);

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            formation.setImage(imageUrl);
        }

        Formation updatedFormation = formationService.updateFormation(id, formation);
        return ResponseEntity.ok(updatedFormation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormation(@PathVariable int id) {
        formationService.deleteFormation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/feedback-count")
    public int getFeedbackCountByFormation(@PathVariable int id) {
        return formationService.getFeedbackCountByFormation(id);
    }

    @GetMapping("/{id}/feedbacks")
    public List<Feedback> getFeedbacksByFormation(@PathVariable int id) {
        Formation formation = formationService.getFormationById(id);
        if (formation != null) {
            return formation.getFeedbacks();
        }
        return new ArrayList<>();
    }

    // New endpoints for managing favorites
    @PostMapping("/favorites/{userId}/{formationId}")
    public ResponseEntity<String> addToFavorites(@PathVariable int userId, @PathVariable int formationId) {
        formationService.addFormationToFavorites(userId, formationId);
        return ResponseEntity.ok("Formation added to favorites");
    }

    @DeleteMapping("/favorites/{userId}/{formationId}")
    public ResponseEntity<String> removeFromFavorites(@PathVariable int userId, @PathVariable int formationId) {
        formationService.removeFormationFromFavorites(userId, formationId);
        return ResponseEntity.ok("Formation removed from favorites");
    }

    @GetMapping("/favorites/{userId}")
    public ResponseEntity<Set<Formation>> getFavorites(@PathVariable int userId) {
        Set<Formation> favorites = formationService.getFavoriteFormations(userId);
        return ResponseEntity.ok(favorites);
    }

    private String saveImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            System.out.println("No image provided");
            return null;
        }
        try {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            System.out.println("Full file path: " + filePath.toString());
            System.out.println("Parent directory: " + filePath.getParent());
            Files.createDirectories(filePath.getParent());
            System.out.println("Directory created or already exists: " + filePath.getParent());
            image.transferTo(filePath.toFile());
            System.out.println("Image saved at: " + filePath.toAbsolutePath());
            return "http://localhost:8081/api/uploads/" + fileName;
        } catch (IOException e) {
            System.err.println("Failed to upload image: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
        }
    }
}