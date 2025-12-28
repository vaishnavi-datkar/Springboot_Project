import { Component, OnInit } from '@angular/core';
import { Doctor } from '../../../interface/doctor';
import { DoctorService } from '../../../services/doctor';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-doctor-form',
  imports: [CommonModule,FormsModule],
  templateUrl: './doctor-form.html',
  styleUrl: './doctor-form.css',
})
export class DoctorForm implements OnInit {
  doctor: Doctor = {
    doctorName: '',
    specialization: '',
    role: 'Doctor',
    email: ''
  };
  
  isEditMode = false;
  doctorId: number | null = null;
  errorMessage = '';

  constructor(
    private doctorService: DoctorService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.doctorId = +params['id'];
        this.loadDoctor(this.doctorId);
      }
    });
  }

  loadDoctor(id: number): void {
    this.doctorService.getDoctorById(id).subscribe({
      next: (data) => {
        this.doctor = data;
      },
      error: (error) => {
        console.error('Error loading doctor', error);
        this.errorMessage = 'Failed to load doctor';
      }
    });
  }

  saveDoctor(): void {
    if (this.isEditMode && this.doctorId) {
      this.doctorService.updateDoctor(this.doctorId, this.doctor).subscribe({
        next: () => {
          this.router.navigate(['/doctors']);
        },
        error: (error) => {
          console.error('Error updating doctor', error);
          this.errorMessage = 'Failed to update doctor';
        }
      });
    } else {
      this.doctorService.createDoctor(this.doctor).subscribe({
        next: () => {
          this.router.navigate(['/doctors']);
        },
        error: (error) => {
          console.error('Error creating doctor', error);
          this.errorMessage = 'Failed to create doctor';
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/doctors']);
  }
}


