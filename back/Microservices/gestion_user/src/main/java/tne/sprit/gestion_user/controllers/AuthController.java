package tne.sprit.gestion_user.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tne.sprit.gestion_user.Dtos.AuthRequest;
import tne.sprit.gestion_user.Dtos.AuthResponse;
import tne.sprit.gestion_user.Dtos.UserDTO;
import tne.sprit.gestion_user.entities.Role;
import tne.sprit.gestion_user.entities.User;
import tne.sprit.gestion_user.reositorys.RoleRepository;
import tne.sprit.gestion_user.reositorys.UserRepository;
import tne.sprit.gestion_user.services.UserDetailsServiceImpl;
import tne.sprit.gestion_user.utils.JwtUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JavaMailSender mailSender;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService,
                          UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,RoleRepository roleRepository, JavaMailSender mailSender) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.mailSender = mailSender;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername())
                ;
        System.out.println(user);
        if (user.isAccountLocked()) {
            System.out.println("Account is blocked");
            return ResponseEntity.status(333).body("Account is blocked");

        }else {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("cin") String cin,
            @RequestParam("address") String address,
            @RequestParam("image") MultipartFile image,
            @RequestParam("roles") String roles) throws IOException {

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCin(cin);
        user.setAddress(address);
        user.setImage(image.getBytes());

        // Parse the roles from the comma-separated string and look up Role entities
        Set<Role> userRoles = Arrays.stream(roles.split(","))
                .map(String::trim)
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
        user.setRoles(userRoles);

        userRepository.save(user);

        // Create a JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");

        return ResponseEntity.ok(response); // This will automatically set Content-Type to application/json
    }
    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> update(
            @RequestParam("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("cin") String cin,
            @RequestParam("address") String address,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        // Fetch the existing user from the database
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update the fields of the existing user instead of creating a new one
        existingUser.setUsername(username);
        existingUser.setEmail(email);
        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setCin(cin);
        existingUser.setAddress(address);

        // Update the image only if a new one is provided
        if (image != null && !image.isEmpty()) {
            existingUser.setImage(image.getBytes());
        } // If image is null, the existing image will remain unchanged

        // Save the updated user
        userRepository.save(existingUser);

        // Return a JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "User updated successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        return ResponseEntity.ok(new UserDTO(user));
    }
    @GetMapping(value = "/profile/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProfileImage() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        return ResponseEntity.ok(user.getImage());
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAll() {
        List<User> u = userRepository.findAll();
        List<UserDTO> ud= u.stream().map(u1 -> new UserDTO(u1)).collect(Collectors.toList());
        System.out.println(ud);
        return ResponseEntity.ok(ud);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        User user = userRepository.findById(id).get();
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PutMapping("/block")
    public ResponseEntity<Map<String, String>> block(@RequestParam("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setAccountLocked(true);
        userRepository.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User blocked successfully");
        return ResponseEntity.ok(response);
    }
    @PutMapping("/unblock")
    public ResponseEntity<Map<String, String>> unblock(@RequestParam("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setAccountLocked(false);
        userRepository.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User blocked successfully");
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam("id") Long id,
                                            @RequestParam("oldPassword") String oldPassword,
                                            @RequestParam("newPassword") String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password changed successfully");
        }else {
            return ResponseEntity.status(222).body("Password does not match");
        }
    }

    @GetMapping("/exist-username")
    public ResponseEntity<?> existusername(@RequestParam("username") String username) {
        User uByUsername = userRepository.findByUsername(username);

        if (uByUsername == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }else {
            UserDTO userDTO = new UserDTO(uByUsername);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }

    }
    @GetMapping("/exist-email")
    public ResponseEntity<?> existemail(@RequestParam("email") String email) {
        User uByEmail= userRepository.findByEmail(email);

        if (uByEmail == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }else {
            UserDTO userDTO = new UserDTO(uByEmail);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        // Find the user by email
        User user = userRepository.findByEmail(email);
        System.out.println(user.getEmail());
        // Generate a password reset token using JwtUtil
        String token = jwtUtil.generatePasswordResetToken(user.getId());

        // Send the email with the reset link
        String resetLink = "http://localhost:4200/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetLink + "\nThis link will expire in 1 hour.");
        mailSender.send(message);

        return ResponseEntity.ok(Map.of("message", "Password reset email sent successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token,
                                           @RequestParam("newPassword") String newPassword) {
        // Validate the token using the new method
        if (!jwtUtil.validatePasswordResetToken(token)) {
            return ResponseEntity.status(400).body("Invalid or expired token");
        }

        // Extract the user ID from the token
        Long userId = jwtUtil.extractUserId(token);

        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Update the user's password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }


}



