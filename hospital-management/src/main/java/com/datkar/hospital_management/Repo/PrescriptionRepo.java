package com.datkar.hospital_management.Repo;

import com.datkar.hospital_management.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepo extends JpaRepository<Prescription,Integer> {

    Optional<Prescription> findByAppointmentId(Long appointmentId);
}
