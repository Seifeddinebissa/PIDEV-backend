// src/main/java/tn/esprit/Gestion_entreprise/repositories/AppointmentRepository.java
package tn.esprit.Gestion_entreprise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.Gestion_entreprise.entities.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.application.studentId = :studentId")
    List<Appointment> findByApplication_StudentId(@Param("studentId") Long studentId);

    Optional<Appointment> findByCalendarEventId(String calendarEventId);
}