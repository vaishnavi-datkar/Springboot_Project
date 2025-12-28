package com.datkar.hospital_management.model.dto;

import lombok.Data;

@Data
public class PrescriptionDTO {
    private Integer id;
    private String pName;
    private String dName;
    private String medicine;
    private String diagnosis;
    private String notes;
}
