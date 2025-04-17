package tne.sprit.gestion_user.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String cin;
        private String lastName;
        private String address;
        private boolean enabled;
        private boolean accountLocked;

        @Lob
        private byte[] image; // Store image as binary data

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        private Set<Role> roles = new HashSet<>();
}
