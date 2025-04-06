package tne.sprit.gestion_user.controllers;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.Arrays;
import java.util.Set;
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

    public AuthController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService,
                          UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      @RequestParam("email") String email,
                                      @RequestParam("image") MultipartFile image,
                                      @RequestParam("roles") String roles) throws IOException {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setImage(image.getBytes());
        // Parse the roles from the comma-separated string and look up Role entities
        Set<Role> userRoles = Arrays.stream(roles.split(","))
                .map(String::trim)
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
        user.setRoles(userRoles);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(new UserDTO(user));
    }
    @GetMapping(value = "/profile/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProfileImage() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(user.getImage());
    }
}



