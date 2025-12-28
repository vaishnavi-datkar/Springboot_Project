import { Component, OnInit } from '@angular/core';
import { Appointment } from '../../../interface/appointment';
import { Doctor } from '../../../interface/doctor';
import { Patient } from '../../../interface/patient';
import { DoctorService } from '../../../services/doctor';
import { ActivatedRoute, Router } from '@angular/router';
import { appointmentService } from '../../../services/appointment';
import { PatientService } from '../../../services/patient';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-appointment-form',
  imports: [CommonModule, FormsModule],
  templateUrl: './appointment-form.html',
  styleUrl: './appointment-form.css',
})
export class AppointmentForm implements OnInit {
  appointment: Appointment = {
    appointmentDate: '',
    patientId: 0,
    doctorId: 0,
    status: 'SCHEDULED'
  };
  
  doctors: Doctor[] = [];
  patients: Patient[] = [];
  isEditMode = false;
  appointmentId: number | null = null;
  errorMessage = '';

  constructor(
    private appointmentService: appointmentService,
    private doctorService: DoctorService,
    private patientService: PatientService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadDoctors();
    this.loadPatients();
    
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.appointmentId = +params['id'];
        this.loadAppointment(this.appointmentId);
      }
    });
  }

  loadDoctors(): void {
  this.doctorService.getAllDoctorsSimple().subscribe({
    next: (doctors) => {
      this.doctors = doctors;
      console.log('Doctors loaded:', this.doctors);
    },
    error: (error) => {
      console.error('Error loading doctors', error);
    }
  });
}

loadPatients(): void {
  this.patientService.getAllPatientsSimple().subscribe({
    next: (patients) => {
      this.patients = patients;
      console.log('Patients loaded:', this.patients);
    },
    error: (error) => {
      console.error('Error loading patients', error);
    }
  });
}

  loadAppointment(id: number): void {
    this.appointmentService.getAppointmentById(id).subscribe({
      next: (data) => {
        this.appointment = {
          id: data.id,
          appointmentDate: data.appointmentDate,
          patientId: data.patientId || 0,
          doctorId: data.doctorId || 0,
          status: data.status
        };
      },
      error: (error) => {
        console.error('Error loading appointment', error);
        this.errorMessage = 'Failed to load appointment';
      }
    });
  }

  saveAppointment(): void {
    if (this.isEditMode && this.appointmentId) {
      this.appointmentService.updateAppointment(this.appointmentId, this.appointment).subscribe({
        next: () => {
          this.router.navigate(['/appointments']);
        },
        error: (error) => {
          console.error('Error updating appointment', error);
          this.errorMessage = 'Failed to update appointment';
        }
      });
    } else {
      this.appointmentService.createAppointment(this.appointment).subscribe({
        next: () => {
          this.router.navigate(['/appointments']);
        },
        error: (error) => {
          console.error('Error creating appointment', error);
          this.errorMessage = 'Failed to create appointment';
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/appointments']);
  }
}



