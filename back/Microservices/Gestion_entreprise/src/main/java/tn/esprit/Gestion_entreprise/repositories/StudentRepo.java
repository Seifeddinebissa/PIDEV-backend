// src/main/java/tn/esprit/Gestion_entreprise/repositories/StudentRepository.java (new, if not exists)
package tn.esprit.Gestion_entreprise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Gestion_entreprise.entities.Student;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Long> {

}