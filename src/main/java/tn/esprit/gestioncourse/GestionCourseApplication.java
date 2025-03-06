package tn.esprit.gestioncourse;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "tn.esprit.gestioncourse")
public class GestionCourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(GestionCourseApplication.class, args);
    }
}
