package tn.esprit.gestionforum_discussion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionforum_discussion.entities.Discussion;
import tn.esprit.gestionforum_discussion.entities.Forum;
import tn.esprit.gestionforum_discussion.repositories.DiscussionRepository;

import java.util.List;

@Service
public class DiscussionService {
    @Autowired
    private DiscussionRepository discussionRepository;
    public List<Discussion> getAllDiscussion(){return discussionRepository.findAll();}
    public Discussion getDiscussionById(Long id){return discussionRepository.findById(id).get();}
    public  Discussion addDiscussion(Discussion discussion){return discussionRepository.save(discussion);}
    public Discussion updateDiscussion(Discussion discussion){return discussionRepository.save(discussion);}
    public void deleteDiscussion(Long id){discussionRepository.deleteById(id);}
}
