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

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}