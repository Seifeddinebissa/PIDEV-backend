package tn.esprit.gestionforum_discussion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionforum_discussion.entities.Forumm;
import tn.esprit.gestionforum_discussion.repositories.ForumRepository;

import java.util.List;

@Service
public class ForumService {
    @Autowired
    private ForumRepository forumRepository;

    public List<Forumm> getAllForum() {
        return forumRepository.findAll();
    }

    public Forumm getForumById(Long id) {
        return forumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum not found with ID: " + id));
    }

    public Forumm addForum(Forumm forumm) {
        return forumRepository.save(forumm);
    }

    public Forumm updateForum(Forumm forumm) {
        return forumRepository.save(forumm);
    }

    public void deleteForum(Long id) {
        forumRepository.deleteById(id);
    }
    public List<Forumm> searchForumsByTitle(String title) {
        return forumRepository.findByTitleContainingIgnoreCase(title);
    }

}
