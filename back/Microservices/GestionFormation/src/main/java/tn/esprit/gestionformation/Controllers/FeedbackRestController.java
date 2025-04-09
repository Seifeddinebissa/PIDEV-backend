package tn.esprit.gestionformation.Controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionformation.Config.EmailService; // Ajout de l’import
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

    @Autowired
    private EmailService emailService; // Injection du service Email

    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable int id) {
        return feedbackService.getFeedbackById(id);
    }

    @PostMapping
    public Feedback createFeedback(@RequestBody Feedback feedback, @RequestParam int formation_id) {
        Formation formation = formationService.getFormationById(formation_id);
        if (formation != null) {
            feedback.setFormation(formation);
            Feedback savedFeedback = feedbackService.saveFeedback(feedback);

            // Envoi de l’email après ajout du feedback
            String subject = "Nouveau commentaire sur " + formation.getTitle();
            String text = "Un nouveau feedback a été ajouté :\n\n" +
                    "Note : " + savedFeedback.getRating() + "/5\n" +
                    "Commentaire : " + savedFeedback.getComment() + "\n" +
                    "Date : " + savedFeedback.getDate();
            emailService.sendEmail("bennarimohamed8@gmail.com", subject, text);

            return savedFeedback;
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteFeedback(@PathVariable int id) {
        feedbackService.deleteFeedback(id);
    }

    @PutMapping("/{id}")
    public Feedback updateFeedback(@PathVariable int id, @RequestBody Feedback feedback) {
        return feedbackService.updateFeedback(id, feedback);
    }

    @GetMapping("/formation/{formationId}/non-hidden")
    public ResponseEntity<List<Feedback>> getNonHiddenFeedbacksByFormation(@PathVariable int formationId) {
        List<Feedback> feedbacks = feedbackService.getNonHiddenFeedbacksByFormationId(formationId);
        return ResponseEntity.ok(feedbacks);
    }
}