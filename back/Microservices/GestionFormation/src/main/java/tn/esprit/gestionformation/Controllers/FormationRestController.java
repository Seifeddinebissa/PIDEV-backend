package tn.esprit.gestionformation.Controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionformation.Entities.Formation;
import tn.esprit.gestionformation.Services.FormationService;
import java.util.List;

@RestController
@RequestMapping("/formations")
public class FormationRestController {

    @Autowired
    private FormationService formationService;

    @GetMapping
    public List<Formation> getAllFormations() {
        return formationService.getAllFormations();
    }

    @GetMapping("/{id}")
    public Formation getFormationById(@PathVariable int id) {
        return formationService.getFormationById(id);
    }

    @PutMapping("/{id}")
    public Formation updateFormation(@PathVariable int id, @RequestBody Formation formation) {
        return formationService.updateFormation(id, formation);
    }

    @PostMapping
    public Formation createFormation(@RequestBody Formation formation) {
        return formationService.saveFormation(formation);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFormation(@PathVariable int id) {
        formationService.deleteFormation(id);
    }
}
