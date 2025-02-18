package tn.esprit.gestionforum_discussion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionforum_discussion.entities.Comment;
import tn.esprit.gestionforum_discussion.entities.Forum;
import tn.esprit.gestionforum_discussion.repositories.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComment(){return commentRepository.findAll();}
    public Comment getCommentById(Long id){return commentRepository.findById(id).get();}
    public  Comment addComment(Comment comment){return commentRepository.save(comment);}
    public Comment updateComment(Comment comment){return commentRepository.save(comment);}
    public void deleteComment(Long id){commentRepository.deleteById(id);}
}
