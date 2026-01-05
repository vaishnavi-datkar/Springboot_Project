package com.datkar.hospital_management.service;

import com.datkar.hospital_management.Repo.DoctorRepo;
import com.datkar.hospital_management.exceptions.ResourceNotFoundException;
import com.datkar.hospital_management.model.Doctor;
import com.datkar.hospital_management.model.dto.DoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepo doctorRepo;

    public Doctor saveDoctor(Doctor doctor){

        return doctorRepo.save(doctor);
    }

    public List<Doctor> getAllDoctor(){

        return doctorRepo.findAll();
    }
    public Doctor getDoctorById(int doctorId ) {
        return doctorRepo.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
    }

    public void deleteDoctor(int doctorId){
        if (!doctorRepo.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        doctorRepo.deleteById(doctorId);

    }
    public List<DoctorDTO> getAllDoctorsDTO() {
        List<Doctor> doctors = doctorRepo.findAll();
        List<DoctorDTO> dtoList = new ArrayList<>();

        for (Doctor doctor : doctors) {
            DoctorDTO dto = new DoctorDTO();
            dto.setDoctorId(doctor.getDoctorId());
            dto.setDoctorName(doctor.getDoctorName());
            dto.setSpecialization(doctor.getSpecialization());
            dtoList.add(dto);
        }
        return dtoList;
    }

}
