package tn.esprit.gestionformation.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String image;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Min(value = 1, message = "Duration must be at least 1 Week")
    private int duration;

    private boolean is_public;

    @Min(value = 0, message = "Price must be at least 0")
    private double price;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formation")
    private List<Feedback> feedbacks;

    @ManyToMany(mappedBy = "favoriteFormations", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> usersWhoFavorited = new HashSet<>();

    // Existing methods...

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isIs_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // For backwards compatibility (you had this method in the original code)
    public boolean Is_public() {
        return is_public;
    }

    // Getter and Setter for usersWhoFavorited (optional, since Lombok provides them)
    public Set<User> getUsersWhoFavorited() {
        return usersWhoFavorited;
    }

    public void setUsersWhoFavorited(Set<User> usersWhoFavorited) {
        this.usersWhoFavorited = usersWhoFavorited;
    }
}