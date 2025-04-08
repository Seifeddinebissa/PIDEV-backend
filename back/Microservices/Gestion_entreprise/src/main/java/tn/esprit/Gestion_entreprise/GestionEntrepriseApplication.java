package tn.esprit.Gestion_entreprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GestionEntrepriseApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionEntrepriseApplication.class, args);
	}

}
