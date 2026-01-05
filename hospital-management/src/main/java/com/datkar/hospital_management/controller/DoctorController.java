package com.datkar.hospital_management.controller;

import com.datkar.hospital_management.model.Doctor;
import com.datkar.hospital_management.model.dto.DoctorDTO;
import com.datkar.hospital_management.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public List<DoctorDTO> getDoctors() {
        return doctorService.getAllDoctorsDTO();
    }


    @GetMapping("/{doctorId}")   //get only by id
    public Doctor  getDoctorById(@PathVariable int doctorId){
        return doctorService.getDoctorById(doctorId);
    }

    @PostMapping
    public Doctor addDoctor(@Valid @RequestBody Doctor doctor){
        return doctorService.saveDoctor(doctor);
    }

    @PutMapping("/{doctorId}")
    public Doctor updateDoctor(@PathVariable int doctorId,@Valid  @RequestBody Doctor doctor){

        return doctorService.saveDoctor(doctor);
    }

    @DeleteMapping("/{doctorId}")
    public String deleteDoctor(@PathVariable int doctorId){
        doctorService.deleteDoctor(doctorId);
        return "Deleted";
    }

}
