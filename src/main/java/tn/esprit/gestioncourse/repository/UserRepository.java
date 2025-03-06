package tn.esprit.gestioncourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestioncourse.entity.Cours;
import tn.esprit.gestioncourse.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
