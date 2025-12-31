package com.datkar.hospital_management.controller;

import com.datkar.hospital_management.Repo.UserRepo;
import com.datkar.hospital_management.model.User;
import com.datkar.hospital_management.model.dto.AuthResponse;
import com.datkar.hospital_management.model.dto.RegisterRequest;
import com.datkar.hospital_management.model.dto.UserLogin;
import com.datkar.hospital_management.config.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLogin userLogin) {
        try {
            // Find user by username
            User user = userRepository.findByUsername(userLogin.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Verify password
            if (!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername());

            // Create response
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setUsername(user.getUsername());


            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            // Validate role
            if (request.getRole() == null || request.getRole().isEmpty()) {
                return ResponseEntity.badRequest().body("Role is required");
            }

            if (!request.getRole().matches("PATIENT|DOCTOR|ADMIN")) {
                return ResponseEntity.badRequest().body("Invalid role. Must be PATIENT, DOCTOR, or ADMIN");
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setRole(request.getRole());

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }
}