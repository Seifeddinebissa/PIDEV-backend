package tn.esprit.gestionforum_discussion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionforum_discussion.entities.Publication;

@Repository
public interface PublicationRepository  extends JpaRepository<Publication, Long> {
}
