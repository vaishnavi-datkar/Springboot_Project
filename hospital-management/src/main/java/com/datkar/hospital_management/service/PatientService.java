package com.datkar.hospital_management.service;

import com.datkar.hospital_management.Repo.PatientRepo;
import com.datkar.hospital_management.exceptions.ResourceNotFoundException;
import com.datkar.hospital_management.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PatientService {

    @Autowired
    private PatientRepo patientRepo;

    public Patient savePatient(Patient patient){
        return patientRepo.save(patient);
    }

    public List<Patient> getAllPatient(){
        return patientRepo.findAll();
    }

    public  Patient getPatientById(long patientId){  //to fetch a element by id

        return patientRepo.findById(patientId).orElseThrow(()-> new ResourceNotFoundException("Patient Noot found with id: " + patientId));//

    }
    public void deletePatient(long patientId){
            if(!patientRepo.existsById(patientId)){
                throw new ResourceNotFoundException("Patient not found with id: " + patientId);
            }
        patientRepo.deleteById(patientId);
    }

}