package tn.esprit.gestioncourse.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestioncourse.entity.Question;
import tn.esprit.gestioncourse.entity.Reponse;
import tn.esprit.gestioncourse.repository.QuestionRepository;
import tn.esprit.gestioncourse.repository.ReponseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReponseService implements serviceReponse{

    @Autowired
    private ReponseRepository reponseRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // Créer une réponse
    @Override
    public Reponse createReponse(Long questionId, Reponse reponse) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question non trouvée"));

        reponse.setQuestion(question);

        return reponseRepository.save(reponse);
    }


    // Récupérer toutes les réponses
    @Override
    public List<Reponse> getAllReponses() {
        return reponseRepository.findAll();
    }

    // Récupérer une réponse par ID
    @Override
    public Optional<Reponse> getReponseById(Long id) {
        return reponseRepository.findById(id);
    }

    // Modifier une réponse
    @Override
    public Reponse updateReponse(Long id, Reponse reponseDetails) {
        return reponseRepository.findById(id).map(reponse -> {
            reponse.setContenu(reponseDetails.getContenu());
            reponse.setCorrect(reponseDetails.isCorrect());
            reponse.setAnswernum(reponseDetails.getAnswernum());
            return reponseRepository.save(reponse);
        }).orElseThrow(() -> new RuntimeException("Réponse non trouvée"));
    }

    // Supprimer une réponse
    @Override
    public void deleteReponse(Long id) {
        reponseRepository.deleteById(id);
    }

    // Ajouter une réponse à une question
    @Override
    public Reponse addReponseToQuestion(Long questionId, Reponse reponse) {
        return questionRepository.findById(questionId).map(question -> {
            reponse.setQuestion(question);
            return reponseRepository.save(reponse);
        }).orElseThrow(() -> new RuntimeException("Question non trouvée"));
    }



    @Override
    public List<Reponse> getReponsesByQuestionId(Long questionId) {
        return questionRepository.findById(questionId)
                .map(reponseRepository::findByQuestion)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

}
