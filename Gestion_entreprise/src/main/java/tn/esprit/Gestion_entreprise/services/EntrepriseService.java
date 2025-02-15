package tn.esprit.Gestion_entreprise.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.Gestion_entreprise.entities.Entreprise;

import java.util.List;

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

    public void deleteCompany(Long id) {
        EntrepriseRepo.deleteById(id);
    }
}
