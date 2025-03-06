package tn.esprit.gestionforum_discussion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication

public class GestionForumDiscussionApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionForumDiscussionApplication.class, args);
	}

}
