import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Prescription } from '../../../interface/prescription';
import { PrescriptionService } from '../../../services/prescriptionservice';
import { Router } from '@angular/router';

@Component({
  selector: 'app-prescription-list',
  imports: [CommonModule],
  templateUrl: './prescription-list.html',
  styleUrl: './prescription-list.css',
})
export class PrescriptionList implements OnInit {
  prescriptions: Prescription[] = [];

  constructor(
    private prescriptionService: PrescriptionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadPrescriptions();
  }

  loadPrescriptions(): void {
    this.prescriptionService.getAllPrescriptions().subscribe({
      next: (data) => {
        this.prescriptions = data;
      },
      error: (error) => {
        console.error('Error loading prescriptions', error);
      }
    });
  }

  deletePrescription(id: number): void {
    if (confirm('Are you sure you want to delete this prescription?')) {
      this.prescriptionService.deletePrescription(id).subscribe({
        next: () => {
          this.loadPrescriptions();
        },
        error: (error) => {
          console.error('Error deleting prescription', error);
        }
      });
    }
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}

