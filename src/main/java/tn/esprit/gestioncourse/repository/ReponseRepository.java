package tn.esprit.gestioncourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestioncourse.entity.Question;
import tn.esprit.gestioncourse.entity.Reponse;

import java.util.List;

public interface ReponseRepository extends JpaRepository<Reponse,Long> {
    List<Reponse> findByQuestion(Question question);

}
