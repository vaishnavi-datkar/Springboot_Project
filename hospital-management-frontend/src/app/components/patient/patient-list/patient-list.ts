import { Component, OnInit } from '@angular/core';
import { Patient } from '../../../interface/patient';
import { Router } from '@angular/router';
import { PatientService } from '../../../services/patient';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-patient-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './patient-list.html',
  styleUrl: './patient-list.css',
})
export class PatientList implements OnInit {
  patients: Patient[] = [];
  totalPages = 0;
  currentPage = 0;
  pageSize = 10;

  constructor(
    private patientService: PatientService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients(): void {
    this.patientService.getAllPatients(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.patients = response.content || response;
        this.totalPages = response.totalPages || 0;
      },
       error: (error) => {
        console.error('Error loading patients', error);
      }
    });
  }

  openAddModal(): void {
    this.router.navigate(['/patients/new']);
  }

  openEditModal(patient: Patient): void {
    this.router.navigate(['/patients/edit', patient.patientId]);
  }

  deletePatient(id: number): void {
    if (confirm('Are you sure you want to delete this patient?')) {
      this.patientService.deletePatient(id).subscribe({
        next: () => {
          this.loadPatients();
        },
        error: (error) => {
          console.error('Error deleting patient', error);
        }
      });
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadPatients();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadPatients();
    }
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}