package com.datkar.hospital_management.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;


    @Column(nullable = false)
    @NotBlank(message = "Patient name is required")
    private String patientName;

   // @NotNull(message = "Age is required")
   // @Min(1)
    private Integer age;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    //@NotBlank(message = "Phone is required")
    private String phone;

    private String role; // No validation - optional

   // @NotBlank(message = "Gender is required")
    private String gender;

   // @NotBlank(message = "Blood group is required")
    private String bloodGroup;

    @Column(name = "user_id")
    private Long userId;  // Links to User table
}