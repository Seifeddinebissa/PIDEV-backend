package tne.sprit.gestion_user.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // e.g., "STUDENT", "ADMIN", "HR", "TRAINER", "COMPANY"

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}