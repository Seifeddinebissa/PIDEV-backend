package tn.esprit.Gestion_entreprise.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private double salary;
    private String location;
    private Date datePosted;
    private Date dateExpiration;
    private String contractType;
    private String experienceLevel;
    private String jobFunction;
    private String jobType;
    private String jobShift;
    private String jobSchedule;
    private String educationLevel;



    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;
    @JsonManagedReference
    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Favorite> favorites;

}
