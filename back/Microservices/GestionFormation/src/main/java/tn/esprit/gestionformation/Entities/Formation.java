package tn.esprit.gestionformation.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
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
    private double price;  // Nouveau champ ajouté

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy="formation")
    private List<Feedback> Feedbacks;

    public List<Feedback> getFeedbacks() {
        return Feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        Feedbacks = feedbacks;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", is_public=" + is_public +
                ", price=" + price +
                '}';
    }

    public Formation(int id, String image, String title, String description, int duration, boolean is_public, double price) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.is_public = is_public;
        this.price = price;
    }



    public Formation() {
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
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

    public boolean Is_public() {
        return is_public;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public boolean isIs_public() {
        return is_public;
    }
}