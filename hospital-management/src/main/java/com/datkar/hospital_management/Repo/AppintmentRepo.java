package com.datkar.hospital_management.Repo;

import com.datkar.hospital_management.model.Appointment;
import com.datkar.hospital_management.model.AppointmentStatus;
import com.datkar.hospital_management.model.Doctor;
import com.datkar.hospital_management.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppintmentRepo extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByPatient(Patient patient);
    Appointment findByAppointmentDate(LocalDateTime appointmentDate);

}
