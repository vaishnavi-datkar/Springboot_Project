package com.datkar.hospital_management.controller;

import com.datkar.hospital_management.config.JwtUtil;
import com.datkar.hospital_management.model.Prescription;
import com.datkar.hospital_management.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "http://localhost:4200")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private JwtUtil jwtUtil;

    // Get prescription by appointmentId
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Prescription> getPrescriptionByAppointment(
            @PathVariable Long appointmentId,
            @RequestHeader("Authorization") String token) {

        return prescriptionService.getPrescriptionByAppointmentId(appointmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Prescription> getAllPrescriptions(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String role = jwtUtil.extractRole(jwt);

        // Only ADMIN can see all prescriptions
        if ("ADMIN".equals(role)) {
            return prescriptionService.getAllPrescriptions();
        }

        throw new RuntimeException("Access denied");
    }

    @GetMapping("/{id}")
    public Prescription getPrescriptionById(@PathVariable int id) {
        return prescriptionService.getPrescriptionById(id);
    }

    @PostMapping
    public Prescription addPrescription(
            @RequestBody Prescription prescription,
            @RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);
        String role = jwtUtil.extractRole(jwt);

        // Only DOCTOR can create prescriptions
        if (!"DOCTOR".equals(role)) {
            throw new RuntimeException("Only doctors can create prescriptions");
        }

        return prescriptionService.savePrescription(prescription);
    }

    @PutMapping("/{id}")
    public Prescription updatePrescription(@RequestBody Prescription prescription) {
        return prescriptionService.savePrescription(prescription);
    }

    @DeleteMapping("/{id}")
    public String deletePrescription(@PathVariable int id) {
        prescriptionService.deletePrescription(id);
        return "Deleted";
    }
}