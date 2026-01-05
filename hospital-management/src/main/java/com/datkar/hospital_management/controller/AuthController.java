package com.datkar.hospital_management.controller;

import com.datkar.hospital_management.Repo.UserRepo;
import com.datkar.hospital_management.model.User;
import com.datkar.hospital_management.model.dto.RegisterRequest;
import com.datkar.hospital_management.model.dto.UserLogin;
import com.datkar.hospital_management.config.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Username already exists"));
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
            }

            if (request.getRole() == null || request.getRole().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Role is required"));
            }

            if (!request.getRole().matches("PATIENT|DOCTOR|ADMIN")) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid role"));
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setRole(request.getRole());

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "User registered successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLogin request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtUtil.generateToken(request.getUsername());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole());
            response.put("username", user.getUsername());
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}