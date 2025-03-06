package tn.esprit.gestionforum_discussion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ForumRequestDTO {
    private String title;
    private String content;
    private int likes;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate datePosted;

    private String imageBase64;
}


