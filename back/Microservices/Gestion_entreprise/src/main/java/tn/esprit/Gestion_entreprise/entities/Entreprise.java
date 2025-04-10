package tn.esprit.Gestion_entreprise.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;



    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Offre> offres;
}
