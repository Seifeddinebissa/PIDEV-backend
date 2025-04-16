package tn.esprit.gestionformation.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.gestionformation.Entities.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query("SELECT f FROM Feedback f WHERE f.formation.id = :formationId AND f.is_hidden = false")
    List<Feedback> findNonHiddenByFormationId(int formationId);

    // Added method to find feedback by user and formation
    @Query("SELECT f FROM Feedback f WHERE f.user.id = :userId AND f.formation.id = :formationId")
    Optional<Feedback> findByUserIdAndFormationId(int userId, int formationId);
}