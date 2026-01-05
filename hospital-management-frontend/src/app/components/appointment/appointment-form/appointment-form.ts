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
import { HttpClient } from '@angular/common/http'; // ADD THIS

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
    private http: HttpClient, 
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
    this.http.get<any>('http://localhost:8091/api/doctors?page=0&size=1000').subscribe({
      next: (response: any) => {
        if (Array.isArray(response)) {
          this.doctors = response;
        } else if (response && response.content) {
          this.doctors = response.content;
        } else {
          this.doctors = [];
        }
        console.log('Doctors loaded:', this.doctors);
      },
      error: (error: any) => {
        console.error('Error loading doctors', error);
        this.errorMessage = 'Failed to load doctors';
      }
    });
  }

  loadPatients(): void {
    this.http.get<any>('http://localhost:8091/api/patients?page=0&size=1000').subscribe({
      next: (response: any) => {
        if (Array.isArray(response)) {
          this.patients = response;
        } else if (response && response.content) {
          this.patients = response.content;
        } else {
          this.patients = [];
        }
        console.log('Patients loaded:', this.patients);
      },
      error: (error: any) => {
        console.error('Error loading patients', error);
        this.errorMessage = 'Failed to load patients';
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
    // Validate before saving
    if (!this.appointment.patientId || this.appointment.patientId === 0) {
      this.errorMessage = 'Please select a patient';
      return;
    }
    
    if (!this.appointment.doctorId || this.appointment.doctorId === 0) {
      this.errorMessage = 'Please select a doctor';
      return;
    }
    
    if (!this.appointment.appointmentDate) {
      this.errorMessage = 'Please select appointment date';
      return;
    }
    
    console.log('Saving appointment:', this.appointment);
    
    if (this.isEditMode && this.appointmentId) {
      this.http.put(`http://localhost:8091/api/appointments/${this.appointmentId}`, this.appointment).subscribe({
        next: () => {
          this.router.navigate(['/appointments']);
        },
        error: (error: any) => {
          console.error('Error updating appointment', error);
          this.errorMessage = 'Failed to update appointment: ' + (error.error?.message || error.message);
        }
      });
    } else {
      this.http.post('http://localhost:8091/api/appointments', this.appointment).subscribe({
        next: () => {
          this.router.navigate(['/appointments']);
        },
        error: (error: any) => {
          console.error('Error creating appointment', error);
          this.errorMessage = 'Failed to create appointment: ' + (error.error?.message || error.message);
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/appointments']);
  }
}