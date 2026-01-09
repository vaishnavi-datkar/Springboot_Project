import { Component, OnInit } from '@angular/core';
import { Appointment } from '../../../interface/appointment';
import { Doctor } from '../../../interface/doctor';
import { Patient } from '../../../interface/patient';
import { DoctorService } from '../../../services/doctor';
import { ActivatedRoute, Router } from '@angular/router';
import { appointmentService } from '../../../services/appointment';
import { PatientService } from '../../../services/patient';
import { AuthService } from '../../../services/auth';  // ADD
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

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
    status: 'CONFIRMED' 
  };
  
  // Patient details for PATIENT role
  patientDetails = {
    name: '',
    phone: '',
    age: 0,
    bloodGroup: ''
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
    private authService: AuthService,  // ADD
    private http: HttpClient, 
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadDoctors();
    
    // Only load patients for ADMIN/DOCTOR
    if (!this.isPatientRole()) {
      this.loadPatients();
    }
    
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.appointmentId = +params['id'];
        this.loadAppointment(this.appointmentId);
      }
    });
  }

  isPatientRole(): boolean {
    return this.authService.getUserRole() === 'PATIENT';
  }

  loadDoctors(): void {
  this.http.get<any>('http://localhost:8091/api/doctors?page=0&size=1000', {
    headers: { 'Authorization': 'Bearer ' + this.authService.getToken() }  // ADD THIS
  }).subscribe({
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
    // Different validation for PATIENT vs ADMIN/DOCTOR
    if (this.isPatientRole()) {
      // Patient form validation
      if (!this.patientDetails.name) {
        this.errorMessage = 'Please enter patient name';
        return;
      }
      if (!this.patientDetails.phone) {
        this.errorMessage = 'Please enter phone number';
        return;
      }
      if (!this.patientDetails.age || this.patientDetails.age <= 0) {
        this.errorMessage = 'Please enter valid age';
        return;
      }
      if (!this.patientDetails.bloodGroup) {
        this.errorMessage = 'Please enter blood group';
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

      // Create appointment data for PATIENT
      const appointmentData = {
        patientName: this.patientDetails.name,
        patientPhone: this.patientDetails.phone,
        patientAge: this.patientDetails.age,
        patientBloodGroup: this.patientDetails.bloodGroup,
        doctorId: this.appointment.doctorId,
        appointmentDate: this.appointment.appointmentDate,
        status: this.appointment.status
      };

      this.http.post('http://localhost:8091/api/appointments', appointmentData, {
        headers: { 'Authorization': 'Bearer ' + this.authService.getToken() }
      }).subscribe({
        next: () => {
          this.router.navigate(['/appointments']);
        },
        error: (error: any) => {
          console.error('Error creating appointment', error);
          this.errorMessage = 'Failed to create appointment: ' + (error.error?.message || error.message);
        }
      });
    } else {
      // ADMIN/DOCTOR form validation
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
        this.http.put(`http://localhost:8091/api/appointments/${this.appointmentId}`, this.appointment, {
          headers: { 'Authorization': 'Bearer ' + this.authService.getToken() }
        }).subscribe({
          next: () => {
            this.router.navigate(['/appointments']);
          },
          error: (error: any) => {
            console.error('Error updating appointment', error);
            this.errorMessage = 'Failed to update appointment: ' + (error.error?.message || error.message);
          }
        });
      } else {
        this.http.post('http://localhost:8091/api/appointments', this.appointment, {
          headers: { 'Authorization': 'Bearer ' + this.authService.getToken() }
        }).subscribe({
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
  }

  cancel(): void {
    this.router.navigate(['/appointments']);
  }
}