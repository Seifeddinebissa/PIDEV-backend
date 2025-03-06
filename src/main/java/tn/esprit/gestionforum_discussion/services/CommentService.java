package tn.esprit.gestionforum_discussion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionforum_discussion.entities.Commentaire;
import tn.esprit.gestionforum_discussion.repositories.CommentRepository;
import tn.esprit.gestionforum_discussion.repositories.ForumRepository;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ForumRepository forumRepository;

    public List<Commentaire> getAllComment() {
        return commentRepository.findAll();
    }

    public Commentaire getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Commentaire addComment(Commentaire commentaire, Long id) {
        commentaire.setForum(forumRepository.findById(id).get());
        return commentRepository.save(commentaire);
    }

    public Commentaire updateComment(Commentaire commentaire) {
        return commentRepository.save(commentaire);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Commentaire> getCommentsByForumId(Long forumId) {
        return commentRepository.findByForummId(forumId);
    }
}
