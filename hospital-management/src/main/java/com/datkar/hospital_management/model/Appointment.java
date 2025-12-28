package com.datkar.hospital_management.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime appointmentDate;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Patient patient;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    @OneToOne(cascade = CascadeType.ALL)
    private Prescription prescription;


}

