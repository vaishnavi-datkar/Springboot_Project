package com.datkar.hospital_management.controller;

import com.datkar.hospital_management.model.Doctor;
import com.datkar.hospital_management.model.Patient;
import com.datkar.hospital_management.service.DoctorService;
import com.datkar.hospital_management.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping
    public List<Patient> getAllPatient(Patient patient){ // get all patient
        return patientService.getAllPatient();
    }

    @GetMapping("/{patientId}")   //get only by id
    public Patient  getPatientById(@PathVariable long patientId){
        return patientService.getPatientById(patientId);
    }

    @PostMapping
    public Patient addPatient(@Valid @RequestBody Patient patient){
        return patientService.savePatient(patient);
    }

    @PutMapping("/{patientId}")  //  id in URL
    public Patient updatePatient(@PathVariable long patientId,@Valid @RequestBody Patient patient){
        return patientService.savePatient(patient);
    }

    @DeleteMapping("/{patientId}")
    public String deletePatient(@PathVariable long patientId){
        patientService.deletePatient(patientId);
        return "Deleted";
    }

}
