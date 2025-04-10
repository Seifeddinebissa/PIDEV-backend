package tn.esprit.gestionforum_discussion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionforum_discussion.entities.Commentaire;
import tn.esprit.gestionforum_discussion.repositories.CommentRepository;
import tn.esprit.gestionforum_discussion.repositories.ForumRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ForumRepository forumRepository;

    public List<Commentaire> getAllComment() {
        return commentRepository.findAll();
    }

    public Commentaire getCommentById(Long id)
    {
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
    // Nouvelle méthode pour incrémenter les likes
    public Commentaire likeComment(Long id) {
        Commentaire commentaire = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));
        commentaire.setLikes(commentaire.getLikes() + 1);
        return commentRepository.save(commentaire);
    }

    // Nouvelle méthode pour incrémenter les dislikes
    public Commentaire dislikeComment(Long id) {
        Commentaire commentaire = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));
        commentaire.setDislikes(commentaire.getDislikes() + 1);
        return commentRepository.save(commentaire);
    }




    public Optional<Commentaire> getCommentaireById(Long id) {
        return commentRepository.findById(id); // Utilisation du JpaRepository pour récupérer le commentaire par ID
    }

    public double getMoyenneLikesDislikesForComment(Commentaire commentaire) {
        int totalLikes = commentaire.getLikes();
        int totalDislikes = commentaire.getDislikes();

        // Calcul de la moyenne des likes et dislikes
        return (totalLikes + totalDislikes) / 2.0;
    }

    public double getAverageLikes() {
        // Fetch all comments and calculate the average of likes
        List<Commentaire> comments = commentRepository.findAll();
        double totalLikes = comments.stream().mapToInt(Commentaire::getLikes).sum();
        return totalLikes / comments.size();
    }

    public double getAverageDislikes() {
        // Fetch all comments and calculate the average of dislikes
        List<Commentaire> comments = commentRepository.findAll();
        double totalDislikes = comments.stream().mapToInt(Commentaire::getDislikes).sum();
        return totalDislikes / comments.size();
    }

}
