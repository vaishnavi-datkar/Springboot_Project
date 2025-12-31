import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Patient } from '../../../interface/patient';
import { PatientService } from '../../../services/patient';

@Component({
  selector: 'app-patient-form',
  imports: [CommonModule, FormsModule],
  templateUrl: './patient-form.html',
  styleUrl: './patient-form.css',
})
export class PatientForm implements OnInit {
  patient: Patient = {
    patientName: '',
    age: 0,
    email: '',
    phone: '',
    gender: '',
    bloodGroup: ''
  };
  
  isEditMode = false;
  patientId: number | null = null;
  errorMessage = '';

  constructor(
    private patientService: PatientService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.patientId = +params['id'];
        this.loadPatient(this.patientId);
      }
    });
     }

  loadPatient(id: number): void {
    this.patientService.getPatientById(id).subscribe({
      next: (data) => {
        this.patient = data;
      },
      error: (error) => {
        console.error('Error loading patient', error);
        this.errorMessage = 'Failed to load patient';
      }
    });
  }

 savePatient(): void {
  // Remove role field before sending
  const patientData = { ...this.patient };
  delete patientData.role;
  
  console.log('Saving patient:', patientData);
  
  if (this.isEditMode && this.patientId) {
    this.patientService.updatePatient(this.patientId, patientData).subscribe({
      next: () => {
        this.router.navigate(['/patients']);
      },
      error: (error) => {
        console.error('Error updating patient', error);
        this.errorMessage = 'Failed to update patient: ' + (error.error?.message || error.message);
      }
    });
  } else {
    this.patientService.createPatient(patientData).subscribe({
      next: () => {
        this.router.navigate(['/patients']);
      },
      error: (error) => {
        console.error('Error creating patient', error);
        this.errorMessage = 'Failed to create patient: ' + (error.error?.message || error.message);
      }
    });
  }
}
  cancel(): void {
    this.router.navigate(['/patients']);
  }
}