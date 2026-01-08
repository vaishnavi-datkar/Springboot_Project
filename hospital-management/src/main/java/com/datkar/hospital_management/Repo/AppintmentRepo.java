package com.datkar.hospital_management.Repo;

import com.datkar.hospital_management.model.Appointment;
import com.datkar.hospital_management.model.AppointmentStatus;
import com.datkar.hospital_management.model.Doctor;
import com.datkar.hospital_management.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppintmentRepo extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByPatient(Patient patient);
    Appointment findByAppointmentDate(LocalDateTime appointmentDate);

    // JOIN FETCH tells Hibernate to load patient and doctor data in the same query instead of separate queries, preventing N+1 problem and ensuring data is available
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.doctor")
    Page<Appointment> findAllWithPatientAndDoctor(Pageable pageable);

    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.doctor WHERE a.doctor.userId = :userId")
    Page<Appointment> findByDoctorUserId(@Param("userId") Long userId, Pageable pageable);

    // For PATIENT - appointments created by user OR patient name matches user's name
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.doctor " +
            "WHERE a.createdBy = :userId OR a.patient.patientName = :patientName")
    Page<Appointment> findByPatientUserIdOrName(@Param("userId") Long userId, @Param("patientName") String patientName, Pageable pageable);
}

