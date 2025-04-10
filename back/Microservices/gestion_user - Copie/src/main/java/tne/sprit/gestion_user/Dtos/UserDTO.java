package tne.sprit.gestion_user.Dtos;

import lombok.*;
import tne.sprit.gestion_user.entities.Role;
import tne.sprit.gestion_user.entities.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String password;
    private String firstName;
    private String cin;
    private String lastName;
    private String address;
    private boolean enabled;
    private boolean accountLocked;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.cin = user.getCin();
        this.lastName = user.getLastName();
        this.address = user.getAddress();
        this.enabled = user.isEnabled();
        this.accountLocked = user.isAccountLocked();
        this.firstName = user.getFirstName();
        this.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}