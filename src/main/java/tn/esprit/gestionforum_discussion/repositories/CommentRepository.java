package tn.esprit.gestionforum_discussion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionforum_discussion.entities.Commentaire;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findByForummId(Long forumId);
    //Page<Commentaire> findByForumId(Long forumId, Pageable pageable);
}
