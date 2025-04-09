package tn.esprit.gestionformation.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionformation.Entities.Feedback;
import tn.esprit.gestionformation.Entities.Formation;
import tn.esprit.gestionformation.Repositories.FeedbackRepository;
import tn.esprit.gestionformation.Repositories.FormationRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FormationRepository formationRepository;

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(int id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(int id) {
        feedbackRepository.deleteById(id);
    }

    public Feedback updateFeedback(int id, Feedback updatedFeedback) {
        return feedbackRepository.findById(id).map(feedback -> {
            feedback.setRating(updatedFeedback.getRating());
            feedback.setComment(updatedFeedback.getComment());
            feedback.setDate(updatedFeedback.getDate());
            feedback.setIs_hidden(updatedFeedback.isIs_hidden());
            return feedbackRepository.save(feedback);
        }).orElse(null);
    }


    // Updated to match repository method
    public List<Feedback> getNonHiddenFeedbacksByFormationId(int formationId) {
        return feedbackRepository.findNonHiddenByFormationId(formationId);
    }

    public List<Map<String, Object>> getAllFormationsFeedbackStats() {
        try {
            List<Formation> formations = formationRepository.findAll();
            if (formations.isEmpty()) {
                System.out.println("Aucune formation trouvée.");
                return Collections.emptyList();
            }

            // Calculer les stats pour chaque formation
            List<Map<String, Object>> allStats = formations.stream().map(formation -> {
                List<Feedback> feedbacks = feedbackRepository.findNonHiddenByFormationId(formation.getId());
                Map<String, Object> stats = new HashMap<>();

                stats.put("formationId", formation.getId());
                stats.put("title", formation.getTitle() != null ? formation.getTitle() : "Titre inconnu");
                stats.put("image", formation.getImage() != null ? formation.getImage() : "assets/img/courses/default.jpg");

                if (feedbacks == null || feedbacks.isEmpty()) {
                    stats.put("average", "0.00");
                    stats.put("stdDev", "0.00");
                    stats.put("totalFeedbacks", 0);
                    stats.put("distribution", Collections.emptyMap());
                } else {
                    double average = feedbacks.stream()
                            .mapToInt(Feedback::getRating)
                            .average()
                            .orElse(0.0);

                    double stdDev = Math.sqrt(feedbacks.stream()
                            .mapToDouble(f -> Math.pow(f.getRating() - average, 2))
                            .average()
                            .orElse(0.0));

                    Map<Integer, Long> distribution = feedbacks.stream()
                            .collect(Collectors.groupingBy(Feedback::getRating, Collectors.counting()));

                    stats.put("average", String.format("%.2f", average));
                    stats.put("stdDev", String.format("%.2f", stdDev));
                    stats.put("totalFeedbacks", feedbacks.size());
                    stats.put("distribution", distribution);
                }
                return stats;
            }).collect(Collectors.toList());

            // Filtrer pour ne garder que les formations avec totalFeedbacks > 0
            return allStats.stream()
                    .filter(stat -> (int) stat.get("totalFeedbacks") > 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erreur dans getAllFormationsFeedbackStats : " + e.getMessage());
            throw new RuntimeException("Erreur lors du calcul des stats", e);
        }
    }
}