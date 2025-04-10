package tn.esprit.gestionforum_discussion.user;



import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private UserStatus status;
    private String avatarUrl;
}
