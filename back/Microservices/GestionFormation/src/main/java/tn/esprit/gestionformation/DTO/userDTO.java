package tn.esprit.gestionformation.DTO;

import lombok.Data;

import java.util.List;

@Data
public class userDTO {
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

    public userDTO(userDTO user) {
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

    }
}
