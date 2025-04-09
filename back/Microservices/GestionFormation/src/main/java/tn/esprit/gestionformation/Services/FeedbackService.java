package tn.esprit.gestionformation.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionformation.Entities.Feedback;
import tn.esprit.gestionformation.Repositories.FeedbackRepository;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

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


}