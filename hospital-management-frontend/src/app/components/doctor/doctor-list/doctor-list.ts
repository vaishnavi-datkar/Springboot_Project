import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from '../../../services/doctor';
import { Doctor } from '../../../interface/doctor';

@Component({
  selector: 'app-doctor-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './doctor-list.html',
  styleUrls: ['./doctor-list.css']
})
export class DoctorList implements OnInit {
  doctors: Doctor[] = [];
  totalPages = 0;
  currentPage = 0;
  pageSize = 10;
  
  showModal = false;
  isEditMode = false;
  selectedDoctor: Doctor = {
    doctorName: '',
    specialization: '',
    role: 'Doctor',
    email: ''
  };

  constructor(
    private doctorService: DoctorService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDoctors();
  }

  loadDoctors(): void {
    this.doctorService.getAllDoctors(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.doctors = response.content;
        this.totalPages = response.totalPages;
      },
      error: (error) => {
        console.error('Error loading doctors', error);
      }
    });
  }

  openAddModal(): void {
    this.isEditMode = false;
    this.selectedDoctor = {
      doctorName: '',
      specialization: '',
      role: 'Doctor',
      email: ''
    };
    this.showModal = true;
  }

  openEditModal(doctor: Doctor): void {
    this.isEditMode = true;
    this.selectedDoctor = { ...doctor };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  saveDoctor(): void {
    if (this.isEditMode) {
      this.doctorService.updateDoctor(this.selectedDoctor.doctorId!, this.selectedDoctor).subscribe({
        next: () => {
          this.loadDoctors();
          this.closeModal();
        },
        error: (error) => {
          console.error('Error updating doctor', error);
        }
      });
    } else {
      this.doctorService.createDoctor(this.selectedDoctor).subscribe({
        next: () => {
          this.loadDoctors();
          this.closeModal();
        },
        error: (error) => {
          console.error('Error creating doctor', error);
        }
      });
    }
  }

  deleteDoctor(id: number): void {
    if (confirm('Are you sure you want to delete this doctor?')) {
      this.doctorService.deleteDoctor(id).subscribe({
        next: () => {
          this.loadDoctors();
        },
        error: (error) => {
          console.error('Error deleting doctor', error);
        }
      });
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadDoctors();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadDoctors();
    }
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}