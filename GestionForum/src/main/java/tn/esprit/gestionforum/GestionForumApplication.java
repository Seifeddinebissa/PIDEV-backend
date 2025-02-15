package tn.esprit.gestionforum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GestionForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionForumApplication.class, args);
	}



}
