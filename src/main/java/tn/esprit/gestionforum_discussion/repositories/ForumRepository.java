package tn.esprit.gestionforum_discussion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionforum_discussion.entities.Forumm;

import java.util.List;

@Repository
public interface ForumRepository extends JpaRepository<Forumm, Long> {
    List<Forumm> findByTitleContainingIgnoreCase(String title);
}
