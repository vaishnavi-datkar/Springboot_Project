package com.datkar.hospital_management.controller;

import com.datkar.hospital_management.model.Appointment;
import com.datkar.hospital_management.model.dto.AppointmentDTO;
import com.datkar.hospital_management.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public Page<AppointmentDTO> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return appointmentService.getAllAppointmentsDTO(page, size);
    }


    @GetMapping("/{id}")
    public Appointment getAppointment(@PathVariable long id){

        return appointmentService.getAppointmentById(id);
    }

    @PostMapping
    public Appointment addAppointment(@RequestBody Appointment appointment){
        return appointmentService.saveAppointment(appointment);
    }


    @PutMapping("/{id}")
    public Appointment updateAppointment(@RequestBody Appointment appointment){
        return appointmentService.saveAppointment(appointment);

    }

    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable long id){
        appointmentService.deleteAppointment(id);
        return "Deleted";
    }
}
