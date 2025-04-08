package tn.esprit.Gestion_entreprise.controllers;

import org.springframework.data.domain.Page; // Correct import for Spring Data pagination
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.Gestion_entreprise.entities.Entreprise;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/companies")
public class EntrepriseController {
    @Autowired
    private tn.esprit.Gestion_entreprise.services.EntrepriseService EntrepriseService;

    @GetMapping
    public Page<Entreprise> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return EntrepriseService.getAllCompanies(page, size);
    }
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Entreprise> updateCompany(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "sector", required = false) String sector,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "website", required = false) String website,
            @RequestParam(value = "logo", required = false) MultipartFile logo
    ) {
        try {
            Entreprise updatedEntreprise = EntrepriseService.updateCompany(id, name, sector, location, description, email, phone, website, logo);
            return ResponseEntity.ok(updatedEntreprise);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{id}")
    public Entreprise getCompanyById(@PathVariable Long id) {
        return EntrepriseService.getCompanyById(id);
    }

    @PostMapping
    public ResponseEntity<Entreprise> createCompany(
            @RequestParam("name") String name,
            @RequestParam(value = "sector", required = false) String sector,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "website", required = false) String website,
            @RequestParam(value = "logo", required = false) MultipartFile logo
    ) {
        try {
            Entreprise entreprise = EntrepriseService.saveCompany(name, sector, location, email, phone, website, logo);
            return ResponseEntity.ok(entreprise);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        EntrepriseService.deleteCompany(id);
        return ResponseEntity.ok("Entreprise deleted successfully");
    }
    @GetMapping(value = "/images/uploads/entreprises/{filename:.+}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String filename) {
        String sanitizedFilename = filename.replace("%20", " ");
        FileSystemResource file = new FileSystemResource("/Users/macbook/Desktop/Back-end/PIDEV-backend/uploads/entreprises/" + sanitizedFilename);
        System.out.println("Attempting to serve image: " + file.getPath());
        if (!file.exists()) {
            System.out.println("File not found: " + file.getPath());
            return ResponseEntity.notFound().build();
        }
        System.out.println("Serving image: " + file.getPath());
        return ResponseEntity.ok()
                .contentType(determineMediaType(sanitizedFilename))
                .body(file);
    }

    private MediaType determineMediaType(String filename) {
        if (filename.toLowerCase().endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }


}

