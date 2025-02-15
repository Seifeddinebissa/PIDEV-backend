package tn.esprit.Gestion_entreprise.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Gestion_entreprise.entities.Offre;
import tn.esprit.Gestion_entreprise.repositories.OffreRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OffreService {

    private final OffreRepo offreRepo;

    public List<Offre> getAllOffres() {
        return offreRepo.findAll();
    }

    public Offre getOffreById(Long id) {
        return offreRepo.findById(id).orElseThrow(() -> new RuntimeException("Offre not found"));
    }

    public Offre addOffre(Offre offre) {
        return offreRepo.save(offre);
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
}
