package com.datkar.hospital_management.Repo;

import com.datkar.hospital_management.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor,Integer> {
    List<Doctor> findBySpecialization(String specialization);

    Doctor findByEmail(String email);
    Optional<Doctor> findByUserId(Long userId);

}
