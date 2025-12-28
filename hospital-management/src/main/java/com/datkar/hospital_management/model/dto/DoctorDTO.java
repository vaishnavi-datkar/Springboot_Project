package com.datkar.hospital_management.model.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class DoctorDTO {

    private Integer doctorId;
    private String doctorName;
    private String specialization;
    private String role = "Doctor";
    private String email;
}
