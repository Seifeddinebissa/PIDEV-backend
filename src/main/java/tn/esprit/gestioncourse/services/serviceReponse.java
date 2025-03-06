package tn.esprit.gestioncourse.services;

import tn.esprit.gestioncourse.entity.Reponse;

import java.util.List;
import java.util.Optional;

public interface serviceReponse {

    public Reponse createReponse(Long questionId, Reponse reponse);
    public List<Reponse> getAllReponses();
    public Optional<Reponse> getReponseById(Long id) ;
    public Reponse updateReponse(Long id, Reponse reponseDetails);
    public void deleteReponse(Long id) ;
    public Reponse addReponseToQuestion(Long questionId, Reponse reponse) ;
    public List<Reponse> getReponsesByQuestionId(Long questionId) ;
}
