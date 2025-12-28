package com.datkar.hospital_management.Repo;

import com.datkar.hospital_management.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepo extends JpaRepository<Patient,Long> {
    List<Patient> findByBloodGroup(String bloodGroup);
    Patient findByEmail(String email);
    List<Patient> findByGender(String gender);

}
