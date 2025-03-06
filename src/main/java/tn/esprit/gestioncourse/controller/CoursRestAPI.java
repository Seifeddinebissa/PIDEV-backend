package tn.esprit.gestioncourse.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.gestioncourse.entity.Cours;
import tn.esprit.gestioncourse.services.CoursService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/cours")
@CrossOrigin(origins = "http://localhost:4200")
public class CoursRestAPI {

    private static final String UPLOAD_DIR = "/Users/dorraamri/Desktop/uploads/";

    @Autowired
    private CoursService coursService;

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<Cours> ajouterCours(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "pdfs", required = false) List<MultipartFile> pdfs) {

        String imageUrl = saveImage(image);
        Cours cours = new Cours();
        cours.setTitre(titre);
        cours.setDescription(description);
        cours.setImage(imageUrl);
        List<String> contenu = savePdfs(pdfs);
        cours.setContenu(contenu);

        Cours savedCours = coursService.ajouterCours(cours);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCours);
    }

    private String saveImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }

        try {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.createDirectories(filePath.getParent());
            image.transferTo(filePath.toFile());
            System.out.println("Image saved at: " + filePath.toAbsolutePath());
            return "http://localhost:8088/dorra/uploads/" + fileName; // Assurez-vous que 8088 est correct
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    private List<String> savePdfs(List<MultipartFile> pdfs) {
        if (pdfs == null || pdfs.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> pdfUrls = new ArrayList<>();
        try {
            for (MultipartFile pdf : pdfs) {
                if (!pdf.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + pdf.getOriginalFilename();
                    Path filePath = Paths.get(UPLOAD_DIR, fileName);
                    Files.createDirectories(filePath.getParent());
                    pdf.transferTo(filePath.toFile());
                    String pdfUrl = "http://localhost:8088/dorra/uploads/" + fileName;
                    pdfUrls.add(pdfUrl);
                }
            }
            return pdfUrls;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload PDFs", e);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Cours>> getAllCours() {
        return ResponseEntity.ok(coursService.getAllCours());
    }

    @GetMapping("/get/{idCours}")
    public ResponseEntity<Cours> getCoursById(@PathVariable Long idCours) {
        Cours cours = coursService.getCoursById(idCours)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        return ResponseEntity.ok(cours);
    }

    @PutMapping(value = "/update/{idCours}", consumes = "multipart/form-data")
    public ResponseEntity<Cours> updateCours(
            @PathVariable Long idCours,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "pdfs", required = false) List<MultipartFile> pdfs,
            @RequestParam(value = "contenu", required = false) String contenuJson) {

        Cours cours = coursService.getCoursById(idCours)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

        cours.setTitre(titre);
        cours.setDescription(description);

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            cours.setImage(imageUrl);
        }

        List<String> updatedContenu = new ArrayList<>();
        if (contenuJson != null && !contenuJson.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                updatedContenu = mapper.readValue(contenuJson, new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erreur lors de la désérialisation de contenuJson", e);
            }
        } else {
            updatedContenu = cours.getContenu() != null ? new ArrayList<>(cours.getContenu()) : new ArrayList<>();
        }

        if (pdfs != null && !pdfs.isEmpty()) {
            List<String> newPdfs = savePdfs(pdfs);
            updatedContenu.addAll(newPdfs);
        }

        cours.setContenu(updatedContenu);
        Cours updatedCours = coursService.updateCours(cours); // Appel corrigé
        return ResponseEntity.ok(updatedCours);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        coursService.deleteCours(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/byTitre")
    public ResponseEntity<Cours> getCoursByTitre(@RequestParam String titre) {
        return coursService.findByTitre(titre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}