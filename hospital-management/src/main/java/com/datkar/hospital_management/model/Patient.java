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
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long patientId;

    @NotBlank(message = "Patient name is required")
    @Size(min = 2, max = 100)
    private String patientName;

    @NotBlank(message = "Age required")
    @Min(value = 0, message = "Age must be positive")
    @Max(value = 150, message = "Age must be realistic")
    private Integer age;

    @NotBlank(message = "email required")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "number required")
    @Pattern(regexp = "^[0-9]{10}$" , message= "Phone must be 10 digits")
    private String phone;

    @Column(nullable = false)
    private String role;

    private String gender;

    private String bloodGroup;


}
