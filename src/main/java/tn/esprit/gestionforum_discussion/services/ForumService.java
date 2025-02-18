package tn.esprit.gestionforum_discussion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionforum_discussion.entities.Forum;
import tn.esprit.gestionforum_discussion.repositories.ForumRepository;

import java.util.List;

@Service
public class ForumService {
    @Autowired
    private ForumRepository forumRepository;

    public List<Forum> getAllForum(){return forumRepository.findAll();}
    public Forum getForumById(Long id){return forumRepository.findById(id).get();}
    public  Forum addForum(Forum forum){return forumRepository.save(forum);}
    public Forum updateForum(Forum forum){return forumRepository.save(forum);}
    public void deleteForum(Long id){forumRepository.deleteById(id);}
}
