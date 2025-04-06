package tne.sprit.gestion_user.reositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import tne.sprit.gestion_user.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
