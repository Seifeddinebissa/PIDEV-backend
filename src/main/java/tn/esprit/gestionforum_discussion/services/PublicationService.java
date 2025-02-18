package tn.esprit.gestionforum_discussion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionforum_discussion.entities.Forum;
import tn.esprit.gestionforum_discussion.entities.Publication;
import tn.esprit.gestionforum_discussion.repositories.PublicationRepository;

import java.util.List;

@Service
public class PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;


    public List<Publication> getAllPublication(){return publicationRepository.findAll();}
    public Publication getPublicationById(Long id){return publicationRepository.findById(id).get();}
    public  Publication addPublication(Publication publication){return publicationRepository.save(publication);}
    public Publication updatePublication(Publication publication){return publicationRepository.save(publication);}
    public void deletePublication(Long id){publicationRepository.deleteById(id);}
}
