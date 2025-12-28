package com.datkar.hospital_management.service;

import com.datkar.hospital_management.Repo.PrescriptionRepo;
import com.datkar.hospital_management.exceptions.ResourceNotFoundException;
import com.datkar.hospital_management.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepo prescriptionRepo;

    public Prescription savePrescription(Prescription prescription) {

        return prescriptionRepo.save(prescription);
    }

    public List<Prescription> getAllPrescriptions() {

        return prescriptionRepo.findAll();
    }

    public Prescription getPrescriptionById(int id) {

        return prescriptionRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Prescription not found with id: " + id));
    }

    public void deletePrescription(int id) {

        prescriptionRepo.deleteById(id);
    }
}
