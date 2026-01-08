package com.datkar.hospital_management.controller;

import com.datkar.hospital_management.Repo.DoctorRepo;
import com.datkar.hospital_management.Repo.PatientRepo;
import com.datkar.hospital_management.Repo.UserRepo;
import com.datkar.hospital_management.model.Doctor;
import com.datkar.hospital_management.model.Patient;

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

    @Autowired
    private PatientRepo patientRepo;  // ADD

    @Autowired
    private DoctorRepo doctorRepo;

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

            // Validate specialization for DOCTOR
            if ("DOCTOR".equals(request.getRole()) &&
                    (request.getSpecialization() == null || request.getSpecialization().isEmpty())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Specialization required for doctors"));
            }

            // Create User
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setRole(request.getRole());
            user.setName(request.getName());

            User savedUser = userRepository.save(user);

            // Auto-create Patient or Doctor entry
            if ("PATIENT".equals(request.getRole())) {
                Patient patient = new Patient();
                patient.setPatientName(request.getName());
                patient.setEmail(request.getEmail());
                patient.setUserId(savedUser.getId());
                // Default values for required fields
                patient.setAge(0);
                patient.setPhone("");
                patient.setGender("");
                patient.setBloodGroup("");
                patientRepo.save(patient);
            } else if ("DOCTOR".equals(request.getRole())) {
                Doctor doctor = new Doctor();
                doctor.setDoctorName(request.getName());
                doctor.setEmail(request.getEmail());
                doctor.setSpecialization(request.getSpecialization());
                doctor.setUserId(savedUser.getId());
                doctorRepo.save(doctor);
            }

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

            String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole());
            response.put("username", user.getUsername());
            response.put("userId", user.getId());
            response.put("name", user.getName());  // ADD
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}