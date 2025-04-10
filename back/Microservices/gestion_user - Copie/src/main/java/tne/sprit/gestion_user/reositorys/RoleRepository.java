package tne.sprit.gestion_user.reositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import tne.sprit.gestion_user.entities.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
