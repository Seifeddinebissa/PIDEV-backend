package tn.esprit.Gestion_entreprise.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.Gestion_entreprise.entities.*;
import tn.esprit.Gestion_entreprise.repositories.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.internet.MimeMessage;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
public class OffreService {

    private final EntrepriseRepo EntrepriseRepo;
    private final OffreRepo offreRepo;
    private final ApplicationRepo ApplicationRepo;
    private final FavoriteRepo FavoriteRepo;
    private final StudentRepo studentRepository; // Added
    private final AppointmentRepo appointmentRepository; // Added
    private final JavaMailSender mailSender; // Added

    private static final Logger log = LoggerFactory.getLogger(OffreService.class);
    @PersistenceContext
    private EntityManager entityManager;

    private static final String CALENDAR_ID = "primary";

    public List<Offre> getAllOffres() {
        return offreRepo.findAll();
    }

    public Offre getOffreById(Long id) {
        return offreRepo.findById(id).orElseThrow(() -> new RuntimeException("Offre not found"));
    }

    public Offre addOffre(Offre offre) {
        if (offre.getEntreprise() != null && offre.getEntreprise().getId() != null) {
            Optional<Entreprise> entrepriseOpt = EntrepriseRepo.findById(offre.getEntreprise().getId());
            if (entrepriseOpt.isPresent()) {
                offre.setEntreprise(entrepriseOpt.get());
                return offreRepo.save(offre);
            } else {
                throw new EntityNotFoundException("Entreprise not found with id: " + offre.getEntreprise().getId());
            }
        }
        return offreRepo.save(offre);
    }

    public Offre updateOffre(Long id, Offre offre) {
        Optional<Offre> existingOffreOpt = offreRepo.findById(id);
        if (existingOffreOpt.isPresent()) {
            Offre updatedOffre = existingOffreOpt.get();
            updatedOffre.setTitle(offre.getTitle());
            updatedOffre.setDescription(offre.getDescription());
            updatedOffre.setSalary(offre.getSalary());
            updatedOffre.setLocation(offre.getLocation());
            updatedOffre.setDatePosted(offre.getDatePosted());
            updatedOffre.setDateExpiration(offre.getDateExpiration());
            updatedOffre.setContractType(offre.getContractType());
            updatedOffre.setExperienceLevel(offre.getExperienceLevel());
            updatedOffre.setJobFunction(offre.getJobFunction());
            updatedOffre.setJobType(offre.getJobType());
            updatedOffre.setJobShift(offre.getJobShift());
            updatedOffre.setJobSchedule(offre.getJobSchedule());
            updatedOffre.setEducationLevel(offre.getEducationLevel());
            return offreRepo.save(updatedOffre);
        } else {
            throw new RuntimeException("Offre with id " + id + " not found!");
        }
    }

    public boolean deleteOffre(Long id) {
        if (!offreRepo.existsById(id)) {
            throw new RuntimeException("Offre not found with id: " + id);
        }
        offreRepo.deleteById(id);
        return true;
    }

    @Transactional
    public boolean apply(Long studentId, Long offerId) {
        try {
            // Validate student and offer
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            Offre offre = offreRepo.findById(offerId)
                    .orElseThrow(() -> new RuntimeException("Offre not found"));

            // Create and save application
            Application application = new Application();
            application.setStudentId(studentId);
            application.setOffre(offre);
            application.setStatus("PENDING");
            Application savedApplication = ApplicationRepo.save(application);

            // Create appointment
            Appointment appointment = new Appointment();
            appointment.setApplication(savedApplication);
            appointment.setInterviewDate(Date.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()).toInstant()));
            appointment.setLocation("Company Office, " + offre.getEntreprise().getLocation());
            appointment.setDetails("Interview for " + offre.getTitle());
            appointmentRepository.save(appointment);

            // Send email
            sendInterviewEmail(student, offre, appointment);

            // Add to Google Calendar
            String eventId = addToGoogleCalendar(student, offre, appointment);
            appointment.setCalendarEventId(eventId);
            appointmentRepository.save(appointment);

            return true;
        } catch (Exception e) {
            log.error("Failed to apply: {}", e.getMessage());
            throw new RuntimeException("Failed to apply: " + e.getMessage());
        }
    }

    private void sendInterviewEmail(Student student, Offre offre, Appointment appointment) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(student.getEmail());
        helper.setSubject("Interview Scheduled for " + offre.getTitle());
        helper.setText(
                "Dear " + student.getFirstName() + ",\n\n" +
                        "Your application for the position '" + offre.getTitle() +
                        "' has been received. We are pleased to invite you for an interview.\n\n" +
                        "Details:\n" +
                        "Date: " + appointment.getInterviewDate() + "\n" +
                        "Location: " + appointment.getLocation() + "\n" +
                        "Additional Info: " + appointment.getDetails() + "\n\n" +
                        "Best regards,\n" + offre.getEntreprise().getName(),
                false
        );

        mailSender.send(message);
    }

    private String addToGoogleCalendar(Student student, Offre offre, Appointment appointment) throws Exception {
        // Load credentials from the service account key JSON file
        InputStream in = getClass().getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new FileNotFoundException("Credentials file not found: /credentials.json");
        }

        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(Collections.singletonList(com.google.api.services.calendar.CalendarScopes.CALENDAR_EVENTS));

        // Build the Calendar service
        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("JobApplication")
                .build();

        // Create the event
        Event event = new Event()
                .setSummary("Interview: " + offre.getTitle())
                .setLocation(appointment.getLocation())
                .setDescription(appointment.getDetails());

        DateTime startDateTime = new DateTime(appointment.getInterviewDate());
        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("UTC");
        event.setStart(start);

        DateTime endDateTime = new DateTime(
                new Date(appointment.getInterviewDate().getTime() + 60 * 60 * 1000) // 1 hour later
        );
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("UTC");
        event.setEnd(end);

        // Insert the event into the calendar
        Event createdEvent = service.events().insert(CALENDAR_ID, event).execute();
        return createdEvent.getId();
    }

    public boolean addFavoris(Long studentId, Long offerId) {
        if (FavoriteRepo.existsByStudentIdAndOffreId(studentId, offerId)) {
            return false;
        }
        Offre offre = offreRepo.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offre not found"));
        Favorite favorite = new Favorite(null, studentId, offre);
        FavoriteRepo.save(favorite);
        return true;
    }

    public String uploadFile(MultipartFile file, Long studentId, Long offerId) {
        try {
            String uploadDir = "uploads/";
            Path path = Paths.get(uploadDir + studentId + "_" + offerId + "_" + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return path.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Transactional
    public boolean removeFavorite(Long studentId, Long offerId) {
        log.info("Checking if favorite exists for studentId: {} and offerId: {}", studentId, offerId);
        if (!FavoriteRepo.existsByStudentIdAndOffreId(studentId, offerId)) {
            log.warn("Favorite not found for studentId: {} and offerId: {}", studentId, offerId);
            return false;
        }
        log.info("Removing favorite for studentId: {} and offerId: {}", studentId, offerId);
        FavoriteRepo.deleteByStudentIdAndOffreId(studentId, offerId);
        return true;
    }

    public List<Offre> getFavoriteOffers(Long studentId) {
        try {
            List<Favorite> favorites = FavoriteRepo.findByStudentId(studentId);
            if (favorites.isEmpty()) {
                throw new RuntimeException("No favorites found for student ID: " + studentId);
            }
            return favorites.stream()
                    .map(Favorite::getOffre)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error fetching favorite offers: " + e.getMessage());
            throw e;
        }
    }

    public List<Application> getApplicationsByStudentId(Long studentId) {
        try {
            TypedQuery<Application> query = entityManager.createQuery(
                    "SELECT a FROM Application a JOIN FETCH a.offre WHERE a.studentId = :studentId",
                    Application.class
            );
            query.setParameter("studentId", studentId);
            List<Application> applications = query.getResultList();
            System.out.println("Fetched applications: " + applications.size());
            return applications;
        } catch (Exception e) {
            System.err.println("Error in getApplicationsByStudentId: " + e.getMessage());
            throw e;
        }
    }

    public List<Offre> getOffresByEntrepriseId(Long entrepriseId) {
        Optional<Entreprise> entrepriseOpt = EntrepriseRepo.findById(entrepriseId);
        if (entrepriseOpt.isPresent()) {
            return offreRepo.findByEntrepriseId(entrepriseId);
        } else {
            throw new EntityNotFoundException("Entreprise not found with id: " + entrepriseId);
        }
    }

    public List<FavoriteStats> getFavoriteAnalytics(int limit) {
        return FavoriteRepo.findTopFavoritedOffers().stream()
                .limit(limit)
                .map(result -> new FavoriteStats((Offre) result[0], ((Long) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    public static class FavoriteStats {
        private final Offre offre;
        private final int favoriteCount;

        public FavoriteStats(Offre offre, int favoriteCount) {
            this.offre = offre;
            this.favoriteCount = favoriteCount;
        }

        public Offre getOffre() { return offre; }
        public int getFavoriteCount() { return favoriteCount; }
    }

    public boolean updateApplicationStatus(Long applicationId, String status, Long entrepriseId) {
        Application application = ApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found with id: " + applicationId));

        Offre offre = application.getOffre();
        if (!offre.getEntreprise().getId().equals(entrepriseId)) {
            throw new RuntimeException("Entreprise ID does not match the associated offer.");
        }

        application.setStatus(status);
        ApplicationRepo.save(application);

        // If the status is "ACCEPTED", send an email and update the appointment
        if ("ACCEPTED".equals(status)) {
            Student student = studentRepository.findById(application.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Appointment appointment = application.getAppointment();
            if (appointment == null) {
                appointment = new Appointment();
                appointment.setApplication(application);
            }

            try {
                sendAcceptanceEmail(student, offre, appointment);
            } catch (Exception e) {
                throw new RuntimeException("Failed to send acceptance email: " + e.getMessage());
            }
        }

        return true;
    }




    private void sendAcceptanceEmail(Student student, Offre offre, Appointment appointment) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(student.getEmail());

        String subject = String.format("🎉 You're Invited: Interview for %s at %s",
                offre.getTitle(), offre.getEntreprise().getName());
        helper.setSubject(subject);

        String htmlBody = String.format("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Interview Invitation</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background-color: #f9f9f9;
                        color: #333;
                        padding: 20px;
                    }
                    .container {
                        max-width: 600px;
                        margin: auto;
                        background: #fff;
                        padding: 30px;
                        border-radius: 10px;
                        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                    }
                    h2 {
                        color: #5e3bea;
                    }
                    .details {
                        margin-top: 20px;
                        font-size: 16px;
                        line-height: 1.6;
                    }
                    .highlight {
                        font-weight: bold;
                        color: #5e3bea;
                    }
                    .footer {
                        margin-top: 40px;
                        font-size: 14px;
                        color: #777;
                        text-align: center;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>🎉 Interview Invitation</h2>
                    <p>Dear <span class="highlight">%s</span>,</p>
                    <p>Congratulations! We’re excited to inform you that your application for the position <span class="highlight">%s</span> at <span class="highlight">%s</span> has been <strong>accepted</strong>.</p>

                    <div class="details">
                        <p>📅 <span class="highlight">Date:</span> %s</p>
                        <p>📍 <span class="highlight">Location:</span> %s</p>
                        <p>🔗 <span class="highlight">Meeting Link:</span> <a href="%s">%s</a></p>
                    </div>

                    <p>We look forward to speaking with you and learning more about your background.</p>

                    <p>Best regards,<br><strong>%s Recruitment Team</strong></p>

                    <div class="footer">
                        © 2025 %s. All rights reserved.
                    </div>
                </div>
            </body>
            </html>
            """,
                student.getFirstName(),
                offre.getTitle(),
                offre.getEntreprise().getName(),
                appointment.getInterviewDate().toString(),
                appointment.getLocation(),
                appointment.getDetails(),
                appointment.getDetails(),
                offre.getEntreprise().getName(),
                offre.getEntreprise().getName()
        );

        helper.setText(htmlBody, true); // true enables HTML
        mailSender.send(message);
    }

    @Transactional
    public void scheduleInterview(Long applicationId, Date interviewDate, String interviewLink) {
        // Fetch the application
        Application application = ApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found with id: " + applicationId));

        Offre offre = application.getOffre();
        Student student = studentRepository.findById(application.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Appointment appointment = application.getAppointment();
        if (appointment == null) {
            appointment = new Appointment();
            appointment.setApplication(application);
        }

        // Update the appointment details
        appointment.setInterviewDate(interviewDate);
        appointment.setLocation("Online"); // Or use the entreprise's location
        appointment.setDetails(interviewLink);

        // Save the updated appointment
        appointmentRepository.save(appointment);

        // Send email notification to the student
        try {
            sendAcceptanceEmail(student, offre, appointment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send acceptance email: " + e.getMessage());
        }

        // Add the interview to Google Calendar
        try {
            String eventId = addToGoogleCalendar(student, offre, appointment);
            appointment.setCalendarEventId(eventId); // Store the event ID in the database
            appointmentRepository.save(appointment); // Save the updated appointment with the event ID
        } catch (Exception e) {
            throw new RuntimeException("Failed to add event to Google Calendar: " + e.getMessage());
        }
    }
    public List<Appointment> getAppointmentsByStudentId(Long studentId) {
        try {
            // Fetch appointments for the given student ID
            return appointmentRepository.findByApplication_StudentId(studentId);
        } catch (Exception e) {
            log.error("Error fetching appointments for student ID {}: {}", studentId, e.getMessage());
            throw new RuntimeException("Failed to fetch appointments: " + e.getMessage());
        }
    }
    // DTO class for transferring event data to the frontend
    public static class EventDTO {
        private String id;
        private String summary;
        private String start;
        private String end;
        private String description;
        private String location;

        public EventDTO() {}

        public EventDTO(String id, String summary, String start, String end, String description, String location) {
            this.id = id;
            this.summary = summary;
            this.start = start;
            this.end = end;
            this.description = description;
            this.location = location;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public String getStart() { return start; }
        public void setStart(String start) { this.start = start; }
        public String getEnd() { return end; }
        public void setEnd(String end) { this.end = end; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
    }

    public List<EventDTO> getStudentEvents(Long studentId) {
        try {
            List<Appointment> appointments = appointmentRepository.findByApplication_StudentId(studentId);
            return appointments.stream().map(appointment -> {
                // Fetch the associated application and offre to get the event summary
                Application application = appointment.getApplication();
                Offre offre = application.getOffre();
                String summary = "Interview: " + offre.getTitle();

                // Calculate end time (1 hour after start)
                Date startDate = appointment.getInterviewDate();
                Date endDate = new Date(startDate.getTime() + 60 * 60 * 1000); // 1 hour later

                return new EventDTO(
                        appointment.getCalendarEventId(),
                        summary,
                        startDate.toInstant().toString(), // ISO 8601 format
                        endDate.toInstant().toString(),
                        appointment.getDetails(),
                        appointment.getLocation()
                );
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching events for student ID {}: {}", studentId, e.getMessage());
            throw new RuntimeException("Failed to fetch events: " + e.getMessage());
        }
    }

    public void deleteApplication(Long id) {
        ApplicationRepo.deleteById(id);
    }

    public boolean deleteInterview(String calendarEventId) {
        try {
            // Assuming you have a method to find an event by calendarEventId
            Optional<Appointment> interview = appointmentRepository.findByCalendarEventId(calendarEventId);

            if (interview.isPresent()) {
                // Delete the interview from the database
                appointmentRepository.delete(interview.get());
                return true;  // Return true if deleted successfully
            } else {
                return false; // Return false if the event doesn't exist
            }
        } catch (DataAccessException e) {
            // Handle database-related exceptions
            throw new RuntimeException("Database error occurred while deleting the interview: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle any other exceptions
            throw new RuntimeException("Error occurred while deleting interview with ID " + calendarEventId + ": " + e.getMessage(), e);
        }
    }

    public String createZoomMeeting(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("topic", "Interview");
        body.put("type", 2);
        body.put("start_time", "2025-04-20T10:00:00Z");
        body.put("duration", 30);
        body.put("timezone", "UTC");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.zoom.us/v2/users/me/meetings", entity, String.class);

        return response.getBody();
    }




}