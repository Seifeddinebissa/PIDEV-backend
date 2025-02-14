package tn.esprit.Gestion_entreprise;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Entreprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String sector;
    private String location;
    private String description;
    private String email;
    private String phone;
    private String website;
    private String logo;


//    @OneToMany(mappedBy = "Entreprise", cascade = CascadeType.ALL)
//    private List<Offre> offres;
}
