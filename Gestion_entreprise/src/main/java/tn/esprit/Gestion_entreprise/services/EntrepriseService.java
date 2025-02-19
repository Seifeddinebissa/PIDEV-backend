package tn.esprit.Gestion_entreprise.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.Gestion_entreprise.entities.Entreprise;

import java.util.List;
import java.util.Optional;

@Service
public class EntrepriseService {
    @Autowired
    private tn.esprit.Gestion_entreprise.repositories.EntrepriseRepo EntrepriseRepo;

    public List<Entreprise> getAllCompanies() {
        return EntrepriseRepo.findAll();
    }

    public Entreprise getCompanyById(Long id) {
        return EntrepriseRepo.findById(id).orElse(null);
    }

    public Entreprise saveCompany(Entreprise Entreprise) {
        return EntrepriseRepo.save(Entreprise);
    }


    public Entreprise updateCompany(Long id, Entreprise entreprise) {
        Optional<Entreprise> existingEntrepriseOpt = EntrepriseRepo.findById(id);

        if (existingEntrepriseOpt.isPresent()) {
            Entreprise existingEntreprise = existingEntrepriseOpt.get();

            existingEntreprise.setName(entreprise.getName());
            existingEntreprise.setSector(entreprise.getSector());
            existingEntreprise.setLocation(entreprise.getLocation());
            existingEntreprise.setDescription(entreprise.getDescription());
            existingEntreprise.setEmail(entreprise.getEmail());
            existingEntreprise.setPhone(entreprise.getPhone());
            existingEntreprise.setWebsite(entreprise.getWebsite());
            existingEntreprise.setLogo(entreprise.getLogo());

            return EntrepriseRepo.save(existingEntreprise);
        } else {
            throw new RuntimeException("Entreprise with id " + id + " not found!");
        }
    }




    public void deleteCompany(Long id) {
        EntrepriseRepo.deleteById(id);
    }
}
