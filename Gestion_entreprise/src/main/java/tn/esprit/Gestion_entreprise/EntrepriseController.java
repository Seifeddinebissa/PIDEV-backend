package tn.esprit.Gestion_entreprise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class EntrepriseController {
    @Autowired
    private EntrepriseService EntrepriseService;

    @GetMapping
    public List<Entreprise> getAllCompanies() {
        return EntrepriseService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public Entreprise getCompanyById(@PathVariable Long id) {
        return EntrepriseService.getCompanyById(id);
    }

    @PostMapping
    public Entreprise createCompany(@RequestBody Entreprise company) {
        return EntrepriseService.saveCompany(company);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Long id) {
        EntrepriseService.deleteCompany(id);
    }
}

