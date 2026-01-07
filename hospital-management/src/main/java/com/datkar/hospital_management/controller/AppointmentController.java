package com.datkar.hospital_management.controller;

import com.datkar.hospital_management.config.JwtUtil;
import com.datkar.hospital_management.model.Appointment;
import com.datkar.hospital_management.model.AppointmentStatus;
import com.datkar.hospital_management.model.Doctor;
import com.datkar.hospital_management.model.Patient;
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
            return appointmentService.getAppointmentsByPatientUserId(userId, page, size);
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
    public Appointment addAppointment(@RequestBody AppointmentDTO appointmentDTO){
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.valueOf(appointmentDTO.getStatus()));

        // Frontend sends patientId as number, we need to create Patient object with that ID
        Patient patient = new Patient();
        patient.setPatientId(appointmentDTO.getPatientId());
        appointment.setPatient(patient);

        // Same for doctor - create Doctor object with the ID from frontend
        Doctor doctor = new Doctor();
        doctor.setDoctorId(appointmentDTO.getDoctorId().intValue());
        appointment.setDoctor(doctor);

        return appointmentService.saveAppointment(appointment);
    }

    // Update existing appointment - same conversion logic as create
    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable long id, @RequestBody AppointmentDTO appointmentDTO){
        Appointment appointment = appointmentService.getAppointmentById(id);
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.valueOf(appointmentDTO.getStatus()));

        // Update patient link
        Patient patient = new Patient();
        patient.setPatientId(appointmentDTO.getPatientId());
        appointment.setPatient(patient);

        // Update doctor link
        Doctor doctor = new Doctor();
        doctor.setDoctorId(appointmentDTO.getDoctorId().intValue());
        appointment.setDoctor(doctor);

        return appointmentService.saveAppointment(appointment);
    }

    // Delete appointment
    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable long id){
        appointmentService.deleteAppointment(id);
        return "Deleted";
    }
}