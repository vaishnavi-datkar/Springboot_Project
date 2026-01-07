package com.datkar.hospital_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;


import java.security.PrivateKey;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Integer doctorId;

    @NotBlank(message = "Doctor name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String doctorName;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Column(nullable = false)
    private String role = "Doctor";

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Column(name = "user_id")
    private Long userId;  // Links to User table
}
