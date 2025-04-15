package tn.esprit.gestionformation.Controllers;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionformation.Entities.Feedback;
import tn.esprit.gestionformation.Entities.Formation;
import tn.esprit.gestionformation.Services.FeedbackService;
import tn.esprit.gestionformation.Services.FormationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedbacks")
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackRestController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FormationService formationService;

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

            try {
                feedbackService.sendFeedbackEmail(savedFeedback, formation, formation_id);
            } catch (MessagingException e) {
                System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            }

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

    @GetMapping("/stats")
    public ResponseEntity<List<Map<String, Object>>> getAllFormationsFeedbackStats() {
        List<Map<String, Object>> stats = feedbackService.getAllFormationsFeedbackStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping(value = "/stats/export/pdf", produces = "application/pdf")
    public ResponseEntity<byte[]> exportStatsToPDF() throws Exception {
        List<Map<String, Object>> stats = feedbackService.getAllFormationsFeedbackStats();
        byte[] pdfBytes = feedbackService.generateFeedbackStatsPDF(stats);

        // Préparer la réponse
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "feedback-stats.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}