package tn.esprit.gestionforum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionforum.entities.Forum;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {

}
