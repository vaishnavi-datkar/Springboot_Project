package com.datkar.hospital_management.model.dto;

import lombok.Data;

@Data
public class PatientDTO {
    private Long patientId;
    private String patientName;
    private Integer age;

    private String email;
    private String phone;
    private String role;
    private String gender;
    private String bloodGroup;
}
