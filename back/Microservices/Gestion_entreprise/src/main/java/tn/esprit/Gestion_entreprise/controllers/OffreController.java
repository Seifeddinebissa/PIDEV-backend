package tn.esprit.Gestion_entreprise.controllers;

import jakarta.ws.rs.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.Gestion_entreprise.entities.Application;
import tn.esprit.Gestion_entreprise.entities.Appointment;
import tn.esprit.Gestion_entreprise.entities.Offre;
import tn.esprit.Gestion_entreprise.entities.Student;
import tn.esprit.Gestion_entreprise.repositories.AppointmentRepo;
import tn.esprit.Gestion_entreprise.repositories.OffreRepo;
import tn.esprit.Gestion_entreprise.repositories.StudentRepo;
import tn.esprit.Gestion_entreprise.services.OffreService;
import tn.esprit.Gestion_entreprise.repositories.ApplicationRepo;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/offres")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OffreController {

    private final OffreService offreService;
    private final ApplicationRepo applicationRepo;
    private final AppointmentRepo appointmentRepo;
    private final StudentRepo studentRepo;
    private final OffreRepo offreRepo;

    @PostMapping
    public ResponseEntity<Offre> createOffre(@RequestBody Offre offre) {
        try {
            return ResponseEntity.ok(offreService.addOffre(offre));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Offre> updateOffre(@PathVariable Long id, @RequestBody Offre offre) {
        return ResponseEntity.ok(offreService.updateOffre(id, offre));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOffre(@PathVariable Long id) {
        boolean isDeleted = offreService.deleteOffre(id);
        return isDeleted
                ? ResponseEntity.ok("Offre deleted successfully")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offre not found");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offre> getOffreById(@PathVariable Long id) {
        return ResponseEntity.ok(offreService.getOffreById(id));
    }


    @GetMapping("/entreprise/{entrepriseId}")
    public ResponseEntity<List<Offre>> getOffresByEntrepriseId(@PathVariable Long entrepriseId) {
        List<Offre> offres = offreService.getOffresByEntrepriseId(entrepriseId);
        return ResponseEntity.ok(offres);
    }

    @GetMapping
    public ResponseEntity<List<Offre>> getAllOffres() {
        return ResponseEntity.ok(offreService.getAllOffres());
    }

    @PostMapping("/apply")

    public ResponseEntity<String> applyToOffer(@RequestBody ApplicationRequest request) {
        boolean success = offreService.apply(request.getStudentId(), request.getOfferId());
        return success
                ? ResponseEntity.ok("Application submitted successfully")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Application failed");
    }

    @PostMapping("/favorites/add")
    public ResponseEntity<String> addFavoris(@RequestBody FavoriteRequest request) {
        boolean success = offreService.addFavoris(request.getStudentId(), request.getOfferId());
        return success
                ? ResponseEntity.ok("Added to favorites successfully")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already in favorites");
    }


    @PostMapping("/favorites/remove")
    public ResponseEntity<String> removeFavorite(@RequestBody FavoriteRequest request) {
        if (request.getStudentId() == null || request.getOfferId() == null) {
            return ResponseEntity.badRequest().body("Invalid request parameters");
        }

        boolean success = offreService.removeFavorite(request.getStudentId(), request.getOfferId());
        if (success) {
            return ResponseEntity.ok("Favorite removed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorite not found or already removed");
        }
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<Offre>> getFavoriteOffers(@RequestParam Long studentId) {
        List<Offre> favorites = offreService.getFavoriteOffers(studentId);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/applications")
    public List<Application> getApplications(@RequestParam Long studentId) {
        return offreService.getApplicationsByStudentId(studentId);
    }

    @GetMapping("/favorites/analytics")
    public ResponseEntity<List<OffreService.FavoriteStats>> getFavoriteAnalytics(
            @RequestParam(value = "limit", defaultValue = "5") int limit
    ) {
        return ResponseEntity.ok(offreService.getFavoriteAnalytics(limit));
    }

    @PutMapping("/applications/{id}/status")
    public ResponseEntity<String> updateApplicationStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        try {
            // Fetch the application by its ID
            Application application = applicationRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

            // Retrieve the associated `Offre` and get the `entrepriseId`
            Offre offre = application.getOffre();
            Long entrepriseId = offre.getEntreprise().getId();  // Fetch entrepriseId from the Offre entity

            // Now call the service method with `applicationId`, `status`, and `entrepriseId`
            offreService.updateApplicationStatus(id, request.getStatus(), entrepriseId);

            return ResponseEntity.ok("Application status updated successfully");
        } catch (Exception e) {
            System.err.println("Error updating application status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating application");
        }
    }


    @PutMapping("/applications/{id}/schedule-interview")
    public ResponseEntity<String> scheduleInterview(@PathVariable Long id, @RequestBody InterviewScheduleRequest request) {
        try {
            offreService.scheduleInterview(id, request.getInterviewDate(), request.getInterviewLink());
            return ResponseEntity.ok("Interview scheduled successfully");
        } catch (Exception e) {
            System.err.println("Error scheduling interview: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error scheduling interview");
        }
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAppointmentsByStudentId(@RequestParam Long studentId) {
        try {
            List<Appointment> appointments = offreService.getAppointmentsByStudentId(studentId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/applications/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        try {
            applicationRepo.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete application");
        }
    }
    @GetMapping("/students/{studentId}/events")
    public List<OffreService.EventDTO> getStudentEvents(@PathVariable Long studentId) {
        return offreService.getStudentEvents(studentId);
    }

    @DeleteMapping("/events/{calendarEventId}")
    public ResponseEntity<String> deleteInterview(@PathVariable String calendarEventId) {
        try {
            // Call the service to delete the interview using calendarEventId
            boolean deleted = offreService.deleteInterview(calendarEventId);

            if (deleted) {
                return ResponseEntity.ok("Interview deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Interview not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting interview: " + e.getMessage());
        }
    }



        @GetMapping("/oauth/callback")
        public ResponseEntity<?> zoomOAuthCallback(@RequestParam("code") String code) {
            // Exchange code for access token using RestTemplate or WebClient
            // Save access token securely (e.g., in DB or session)
            return ResponseEntity.ok("Zoom OAuth successful!");
        }
    @GetMapping("/test-email/{applicationId}")
    public ResponseEntity<String> testEmail(@PathVariable Long applicationId) {
        try {
            // Fetch the application by its ID
            Application application = applicationRepo.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

            // Fetch the associated offer and entrepriseId dynamically
            Offre offre = application.getOffre();
            Long entrepriseId = offre.getEntreprise().getId();  // Assuming `getEntreprise()` gives you the company, and `getId()` fetches the entreprise ID

            // Now, pass the entrepriseId to your service
            boolean success = offreService.updateApplicationStatus(applicationId, "ACCEPTED", entrepriseId);

            if (success) {
                return ResponseEntity.ok("Email and calendar event sent!");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to update application status.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }





//    @PostMapping("/schedule")
//    public ResponseEntity<String> scheduleMeeting(@RequestBody MeetingDetails meetingDetails) {
//        String accessToken = offreService.getAccessToken(); // Fetch the stored access token
//
//        if (accessToken == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access token is missing or expired");
//        }
//
//        String url = "https://api.zoom.us/v2/users/me/meetings";
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(accessToken);
//
//        Map<String, Object> meetingRequest = new HashMap<>();
//        meetingRequest.put("topic", meetingDetails.getTopic());
//        meetingRequest.put("type", 2); // Type 2 is for scheduled meetings
//        meetingRequest.put("start_time", meetingDetails.getStartTime());
//        meetingRequest.put("duration", meetingDetails.getDuration());
//        meetingRequest.put("timezone", "America/New_York");
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(meetingRequest, headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.POST,
//                    request,
//                    String.class
//            );
//
//            return ResponseEntity.ok(response.getBody());
//        } catch (HttpStatusCodeException e) {
//            return ResponseEntity.status(e.getStatusCode()).body("Error scheduling meeting: " + e.getResponseBodyAsString());
//        }
//    }





    // Inner classes for request bodies
    static class ApplicationRequest {
        private Long studentId;
        private Long offerId;

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public Long getOfferId() { return offerId; }
        public void setOfferId(Long offerId) { this.offerId = offerId; }
    }

    static class FavoriteRequest {
        private Long studentId;
        private Long offerId;

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public Long getOfferId() { return offerId; }
        public void setOfferId(Long offerId) { this.offerId = offerId; }
    }

    static class StatusRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    static class InterviewScheduleRequest {
        private Date interviewDate;
        private String interviewLink;

        public Date getInterviewDate() { return interviewDate; }
        public void setInterviewDate(Date interviewDate) { this.interviewDate = interviewDate; }

        public String getInterviewLink() { return interviewLink; }
        public void setInterviewLink(String interviewLink) { this.interviewLink = interviewLink; }
    }
}