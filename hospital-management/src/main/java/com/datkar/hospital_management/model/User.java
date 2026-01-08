package com.datkar.hospital_management.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "user_name", unique = true, length = 100, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @Column(nullable = false)
    private String role; // for ADMIN,DOCTOR,PATIENT

    private String name;  // ADD THIS - actual person's name like original name instead of username



}
