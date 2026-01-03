package com.datkar.hospital_management.controller;

import com.datkar.hospital_management.model.Appointment;
import com.datkar.hospital_management.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")

public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public List<Appointment> getAllAppointments(){
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointment getAppointment(@PathVariable long id){

        return appointmentService.getAppointmentById(id);
    }

    @PostMapping
    public Appointment addAppointment(@RequestBody Appointment appointment){
        appointmentService.addAppointment(appointment);
        return appointmentService.saveAppointment(appointment);    }

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
