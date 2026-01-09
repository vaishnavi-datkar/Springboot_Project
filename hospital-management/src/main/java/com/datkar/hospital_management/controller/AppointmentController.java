package com.datkar.hospital_management.controller;

import com.datkar.hospital_management.Repo.DoctorRepo;
import com.datkar.hospital_management.Repo.PatientRepo;
import com.datkar.hospital_management.Repo.UserRepo;
import com.datkar.hospital_management.config.JwtUtil;
import com.datkar.hospital_management.model.*;
import com.datkar.hospital_management.model.dto.AppointmentDTO;
import com.datkar.hospital_management.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    // Get all appointments with pagination - returns patient and doctor names
    @GetMapping
    public Page<AppointmentDTO> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);  // Remove "Bearer "
        String role = jwtUtil.extractRole(jwt);
        Long userId = jwtUtil.extractUserId(jwt);

        // ADMIN sees all appointments
        if ("ADMIN".equals(role)) {
            return appointmentService.getAllAppointmentsDTO(page, size);
        }

        // DOCTOR sees only their appointments
        if ("DOCTOR".equals(role)) {
            return appointmentService.getAppointmentsByDoctorUserId(userId, page, size);
        }

        // PATIENT sees only their appointments
        if ("PATIENT".equals(role)) {
            User user = userRepo.findById(userId).orElseThrow();  // ADD THIS LINE
            return appointmentService.getAppointmentsByPatientUserId(userId, user.getName(), page, size);
        }

        throw new RuntimeException("Invalid role");
    }

    // Get single appointment by ID
    @GetMapping("/{id}")
    public Appointment getAppointment(@PathVariable long id){
        return appointmentService.getAppointmentById(id);
    }

    // Create new appointment - converts patientId/doctorId from frontend to actual Patient/Doctor objects
    @PostMapping
    public Appointment addAppointment(
            @RequestBody AppointmentDTO appointmentDTO,
            @RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);
        Long userId = jwtUtil.extractUserId(jwt);

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.valueOf(appointmentDTO.getStatus()));
        appointment.setCreatedBy(userId);  // ADD THIS LINE

        // Create or update patient
        Patient patient;
        if (appointmentDTO.getPatientId() != null) {
            patient = patientRepo.findById(appointmentDTO.getPatientId()).orElseThrow();
        } else {
            patient = new Patient();
            patient.setPatientName(appointmentDTO.getPatientName());
            patient.setPhone(appointmentDTO.getPatientPhone());
            patient.setAge(appointmentDTO.getPatientAge());
            patient.setBloodGroup(appointmentDTO.getPatientBloodGroup());
            patient.setEmail("");
            patient.setGender("");
            patient = patientRepo.save(patient);
        }
        appointment.setPatient(patient);

        Doctor doctor = new Doctor();
        doctor.setDoctorId(appointmentDTO.getDoctorId().intValue());
        appointment.setDoctor(doctor);

        return appointmentService.saveAppointment(appointment);
    }
    @PutMapping("/{id}")
    public Appointment updateAppointment(
            @PathVariable long id,
            @RequestBody AppointmentDTO appointmentDTO,
            @RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);
        Long userId = jwtUtil.extractUserId(jwt);

        Appointment appointment = appointmentService.getAppointmentById(id);
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.valueOf(appointmentDTO.getStatus()));

        Patient patient = new Patient();
        patient.setPatientId(appointmentDTO.getPatientId());
        appointment.setPatient(patient);
        // Update doctor link
        Doctor doctor = new Doctor();
        doctor.setDoctorId(appointmentDTO.getDoctorId().intValue());
        appointment.setDoctor(doctor);

        return appointmentService.saveAppointment(appointment);
    }

    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable long id){
        appointmentService.deleteAppointment(id);
        return "Deleted";
    }
}