package tn.esprit.gestionpaiementnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GestionPaiementNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionPaiementNotificationApplication.class, args);
	}

}
