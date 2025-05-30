package tn.esprit.gestionformation.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestionformation.Entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}