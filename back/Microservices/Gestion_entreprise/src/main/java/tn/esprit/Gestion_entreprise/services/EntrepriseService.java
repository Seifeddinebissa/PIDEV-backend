package tn.esprit.Gestion_entreprise.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tn.esprit.Gestion_entreprise.entities.Entreprise;
import tn.esprit.Gestion_entreprise.entities.Offre;
import tn.esprit.Gestion_entreprise.repositories.FavoriteRepo;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.io.IOException;

@Service
public class EntrepriseService {

    @Autowired
    private FavoriteRepo favoriteRepo;
    @Autowired
    private tn.esprit.Gestion_entreprise.repositories.EntrepriseRepo EntrepriseRepo;

    public Page<Entreprise> getAllCompanies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return EntrepriseRepo.findAll(pageable); // Updated to match field name
    }

    public Entreprise getCompanyById(Long id) {
        return EntrepriseRepo.findById(id).orElse(null);
    }

    public Entreprise saveCompany(String name, String sector, String location, String email, String phone, String website, MultipartFile logo) throws IOException {
        Entreprise entreprise = new Entreprise();
        entreprise.setName(name);
        entreprise.setSector(sector);
        entreprise.setLocation(location);
        entreprise.setEmail(email);
        entreprise.setPhone(phone);
        entreprise.setWebsite(website);

        if (logo != null && !logo.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + logo.getOriginalFilename();
            Path filePath = Paths.get("uploads/entreprises/" + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, logo.getBytes());
            entreprise.setLogo("/uploads/entreprises/" + fileName); // Store relative path
        }

        return EntrepriseRepo.save(entreprise);
    }


    public Entreprise updateCompany(Long id, String name, String sector, String location, String description, String email, String phone, String website, MultipartFile logo) throws IOException {
        Optional<Entreprise> existingEntrepriseOpt = EntrepriseRepo.findById(id);

        if (existingEntrepriseOpt.isPresent()) {
            Entreprise existingEntreprise = existingEntrepriseOpt.get();

            existingEntreprise.setName(name);
            existingEntreprise.setSector(sector);
            existingEntreprise.setLocation(location);
            existingEntreprise.setDescription(description);
            existingEntreprise.setEmail(email);
            existingEntreprise.setPhone(phone);
            existingEntreprise.setWebsite(website);

            if (logo != null && !logo.isEmpty()) {
                String originalFilename = logo.getOriginalFilename();
                String sanitizedFilename = System.currentTimeMillis() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
                Path filePath = Paths.get("/Users/macbook/Desktop/Back-end/PIDEV-backend/uploads/entreprises/" + sanitizedFilename);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, logo.getBytes());
                System.out.println("Image saved at: " + filePath.toAbsolutePath());
                existingEntreprise.setLogo("/uploads/entreprises/" + sanitizedFilename);
            }

            return EntrepriseRepo.save(existingEntreprise);
        } else {
            throw new RuntimeException("Entreprise with id " + id + " not found!");
        }
    }




    @Transactional
    public void deleteCompany(Long id) {
        if (EntrepriseRepo.existsById(id)) {
            // Delete dependent entities first
            Entreprise entreprise = EntrepriseRepo.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));

            for (Offre offre : entreprise.getOffres()) {
                // Delete related favorites for each offer
                favoriteRepo.deleteByOffreId(offre.getId());
                System.out.println("Deleted favorites for offer ID: " + offre.getId());
            }

            // Now delete the Entreprise
            EntrepriseRepo.deleteById(id);
            System.out.println("Deleted company with ID: " + id);
        } else {
            System.out.println("Company not found.");
        }
    }








}
