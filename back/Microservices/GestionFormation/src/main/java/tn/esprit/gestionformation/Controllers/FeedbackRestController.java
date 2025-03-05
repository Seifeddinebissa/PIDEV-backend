package tn.esprit.gestionformation.Controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionformation.Entities.Feedback;
import tn.esprit.gestionformation.Entities.Formation;
import tn.esprit.gestionformation.Services.FeedbackService;
import tn.esprit.gestionformation.Services.FormationService;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackRestController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FormationService formationService;

    // Get all feedbacks
    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    // Get feedback by ID
    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable int id) {
        return feedbackService.getFeedbackById(id);
    }

    // Create new feedback
    @PostMapping
    public Feedback createFeedback(@RequestBody Feedback feedback, @RequestParam int formation_id) {
        // Get the Formation by ID
        Formation formation = formationService.getFormationById(formation_id);

        if (formation != null) {
            // Set the formation to feedback
            feedback.setFormation(formation);
            return feedbackService.saveFeedback(feedback);
        }

        // If formation doesn't exist, return null or throw exception (you can adjust based on your preference)
        return null;
    }

    // Delete feedback by ID
    @DeleteMapping("/{id}")
    public void deleteFeedback(@PathVariable int id) {
        feedbackService.deleteFeedback(id);
    }

    @PutMapping("/{id}")
    public Feedback updateFeedback(@PathVariable int id, @RequestBody Feedback feedback) {
        return feedbackService.updateFeedback(id, feedback);
    }
}
