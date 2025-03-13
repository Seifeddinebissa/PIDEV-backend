package tn.esprit.Gestion_entreprise.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.Gestion_entreprise.entities.Application;
import tn.esprit.Gestion_entreprise.entities.Entreprise;
import tn.esprit.Gestion_entreprise.entities.Favorite;
import tn.esprit.Gestion_entreprise.entities.Offre;
import tn.esprit.Gestion_entreprise.repositories.ApplicationRepo;
import tn.esprit.Gestion_entreprise.repositories.FavoriteRepo;
import tn.esprit.Gestion_entreprise.repositories.OffreRepo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class OffreService {

    private final tn.esprit.Gestion_entreprise.repositories.EntrepriseRepo EntrepriseRepo;
    private final OffreRepo offreRepo;
    private final ApplicationRepo ApplicationRepo;
    private final FavoriteRepo FavoriteRepo;
    private static final Logger log = LoggerFactory.getLogger(OffreService.class);
    @PersistenceContext
    private EntityManager entityManager;


    public List<Offre> getAllOffres() {
        return offreRepo.findAll();
    }

    public Offre getOffreById(Long id) {
        return offreRepo.findById(id).orElseThrow(() -> new RuntimeException("Offre not found"));
    }

    public Offre addOffre(Offre offre) {
        if (offre.getEntreprise() != null && offre.getEntreprise().getId() != null) {
            // Check if the Entreprise exists
            Optional<Entreprise> entrepriseOpt = EntrepriseRepo.findById(offre.getEntreprise().getId());
            if (entrepriseOpt.isPresent()) {
                offre.setEntreprise(entrepriseOpt.get()); // Set the correct Entreprise object
                return offreRepo.save(offre); // Save the offer
            } else {
                throw new EntityNotFoundException("Entreprise not found with id: " + offre.getEntreprise().getId());
            }
        }
        return offreRepo.save(offre); // Save offer without entreprise if entreprise is not provided
    }

    public Offre updateOffre(Long id, Offre offre) {
        Optional<Offre> existingOffreOpt = offreRepo.findById(id);
        if (existingOffreOpt.isPresent()) {
            Offre updatedOffre = existingOffreOpt.get();
            updatedOffre.setTitle(offre.getTitle());  // Fixed field name
            updatedOffre.setDescription(offre.getDescription());
            updatedOffre.setSalary(offre.getSalary());
            updatedOffre.setLocation(offre.getLocation());
            updatedOffre.setDatePosted(offre.getDatePosted());
            updatedOffre.setDateExpiration(offre.getDateExpiration());
            updatedOffre.setContractType(offre.getContractType());
            updatedOffre.setExperienceLevel(offre.getExperienceLevel());
            updatedOffre.setJobFunction(offre.getJobFunction());
            updatedOffre.setJobType(offre.getJobType());
            updatedOffre.setJobShift(offre.getJobShift());
            updatedOffre.setJobSchedule(offre.getJobSchedule());
            updatedOffre.setEducationLevel(offre.getEducationLevel());


            return offreRepo.save(updatedOffre);
        } else {
            throw new RuntimeException("Offre with id " + id + " not found!");
        }
    }

    public boolean deleteOffre(Long id) {
        if (!offreRepo.existsById(id)) {
            throw new RuntimeException("Offre not found with id: " + id);
        }
        offreRepo.deleteById(id);
        return true;
    }

    // Apply to an offer (placeholder, non-persistent)
    public boolean apply(Long studentId, Long offerId) {
        Offre offre = offreRepo.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offre not found"));

        Application application = new Application();
        application.setStudentId(studentId);
        application.setOffre(offre);

        ApplicationRepo.save(application);
        return true;
    }

     //Add to favorites
    public boolean addFavoris(Long studentId, Long offerId) {
        if (FavoriteRepo.existsByStudentIdAndOffreId(studentId, offerId)) { // Instance method
            return false;
        }
        Offre offre = offreRepo.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offre not found"));
        Favorite favorite = new Favorite(null, studentId, offre);
        FavoriteRepo.save(favorite);
        return true;
    }

    // Upload file (CV)
    public String uploadFile(MultipartFile file, Long studentId, Long offerId) {
        try {
            String uploadDir = "uploads/";
            Path path = Paths.get(uploadDir + studentId + "_" + offerId + "_" + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            // No Application entity, so we just return the file path
            return path.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }
    @Transactional
    public boolean removeFavorite(Long studentId, Long offerId) {
        log.info("Checking if favorite exists for studentId: {} and offerId: {}", studentId, offerId);
        if (!FavoriteRepo.existsByStudentIdAndOffreId(studentId, offerId)) {
            log.warn("Favorite not found for studentId: {} and offerId: {}", studentId, offerId);
            return false;
        }
        log.info("Removing favorite for studentId: {} and offerId: {}", studentId, offerId);
        FavoriteRepo.deleteByStudentIdAndOffreId(studentId, offerId);
        return true;
    }





    public List<Offre> getFavoriteOffers(Long studentId) {
        try {
            List<Favorite> favorites = FavoriteRepo.findByStudentId(studentId);
            if (favorites.isEmpty()) {
                throw new RuntimeException("No favorites found for student ID: " + studentId);
            }
            return favorites.stream()
                    .map(Favorite::getOffre)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error fetching favorite offers: " + e.getMessage());
            throw e; // Rethrow the exception to show in the logs
        }
    }

    public List<Application> getApplicationsByStudentId(Long studentId) {
        try {
            TypedQuery<Application> query = entityManager.createQuery(
                    "SELECT a FROM Application a JOIN FETCH a.offre WHERE a.studentId = :studentId",
                    Application.class
            );
            query.setParameter("studentId", studentId);
            List<Application> applications = query.getResultList();
            System.out.println("Fetched applications: " + applications.size());
            return applications;
        } catch (Exception e) {
            System.err.println("Error in getApplicationsByStudentId: " + e.getMessage());
            throw e; // Re-throw to see stack trace in logs
        }
    }

    public List<Offre> getOffresByEntrepriseId(Long entrepriseId) {
        // Check if the Entreprise exists
        Optional<Entreprise> entrepriseOpt = EntrepriseRepo.findById(entrepriseId);

        if (entrepriseOpt.isPresent()) {
            // If the Entreprise exists, find all offers associated with that Entreprise
            return offreRepo.findByEntrepriseId(entrepriseId);
        } else {
            throw new EntityNotFoundException("Entreprise not found with id: " + entrepriseId);
        }
    }
    public List<FavoriteStats> getFavoriteAnalytics(int limit) {
        return FavoriteRepo.findTopFavoritedOffers().stream()
                .limit(limit)
                .map(result -> new FavoriteStats((Offre) result[0], ((Long) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    public static class FavoriteStats {
        private final Offre offre;
        private final int favoriteCount;

        public FavoriteStats(Offre offre, int favoriteCount) {
            this.offre = offre;
            this.favoriteCount = favoriteCount;
        }

        public Offre getOffre() { return offre; }
        public int getFavoriteCount() { return favoriteCount; }
    }

}
