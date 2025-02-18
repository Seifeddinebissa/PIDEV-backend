package tn.esprit.gestionforum_discussion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionforum_discussion.entities.Message;
@Repository
public interface MessageReposiitory extends JpaRepository<Message, Long> {
}
