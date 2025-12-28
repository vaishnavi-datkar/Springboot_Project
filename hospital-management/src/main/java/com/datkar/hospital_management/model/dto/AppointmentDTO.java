package com.datkar.hospital_management.model.dto;

import com.datkar.hospital_management.model.AppointmentStatus;
import com.datkar.hospital_management.model.Doctor;
import com.datkar.hospital_management.model.Patient;
import com.datkar.hospital_management.model.Prescription;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AppointmentDTO {
    private Long id;
    private LocalDateTime appointmentDate;

    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String status;
    private Long prescriptionId;

}
