package tn.esprit.Gestion_entreprise.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;
}
