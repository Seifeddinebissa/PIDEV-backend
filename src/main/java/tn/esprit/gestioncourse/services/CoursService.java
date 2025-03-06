package tn.esprit.gestioncourse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.gestioncourse.entity.Cours;
import tn.esprit.gestioncourse.entity.User;
import tn.esprit.gestioncourse.repository.CoursRepository;
import tn.esprit.gestioncourse.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
public class CoursService implements service{


    private static final String UPLOAD_DIR = "uploads/";


        @Autowired
        private CoursRepository coursRepository;

        @Autowired
        private UserRepository userRepository;

        // 1. Ajouter un cours
        @Override
        public Cours ajouterCours(Cours cours) {
            // Définir toujours l'ID de l'utilisateur à 1
            Long userId = 1L;

            // Vérifie si l'utilisateur avec ID = 1 existe en base
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur avec ID 1 non trouvé"));

            // Associe toujours l'utilisateur avec ID = 1 au cours
            cours.setUser(user);



            return coursRepository.save(cours);
        }

    @Override
    public Map<String, String> upload(MultipartFile image) {
        if (image.isEmpty()) {
            return Collections.singletonMap("error", "Aucune image fournie");
        }

        try {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "http://localhost:8088/uploads/" + fileName;
            return Collections.singletonMap("imageUrl", imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de l'image", e);
        }
    }


    // 2. Récupérer un cours par son ID
    @Override
        public Optional<Cours> getCoursById(Long id) {
            return coursRepository.findById(id);
        }



        // 3. Récupérer tous les cours
        @Override
        public List<Cours> getAllCours() {
            return coursRepository.findAll();
        }

        // 4. Mettre à jour un cours
        @Override
        public Cours updateCours(Cours cours) {
            return coursRepository.save(cours);
        }
        // 5. Supprimer un cours
        @Override
        public void deleteCours(Long id) {
            coursRepository.deleteById(id);
        }

   @Override
    public Optional<Cours> findByTitre(String titre) {
        return coursRepository.findByTitre(titre);
    }


}