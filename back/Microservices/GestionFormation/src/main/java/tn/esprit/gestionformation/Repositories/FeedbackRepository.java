package tn.esprit.gestionformation.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestionformation.Entities.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
