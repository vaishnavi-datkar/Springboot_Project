import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Appointment } from '../../../interface/appointment';
import { appointmentService } from '../../../services/appointment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-appointment-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './appointment-list.html',
  styleUrl: './appointment-list.css',
})
export class AppointmentList implements OnInit {
  appointments: Appointment[] = [];
  totalPages = 0;
  currentPage = 0;
  pageSize = 10;
  
  showModal = false;
  isEditMode = false;
  selectedAppointment: Appointment = {
    appointmentDate: '',
    patientId: 0,
    doctorId: 0,
    status: 'SCHEDULED'
  };

  constructor(
    private appointmentService: appointmentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAppointments();
  }
loadAppointments(): void {
    this.appointmentService.getAllAppointments(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.appointments = response.content || response;
        this.totalPages = response.totalPages || 0;
      },
      error: (error) => {
        console.error('Error loading appointments', error);
      }
    });
  }

  openAddModal(): void {
    this.router.navigate(['/appointments/new']);
  }

  openEditModal(appointment: Appointment): void {
    this.router.navigate(['/appointments/edit', appointment.id]);
  }

  deleteAppointment(id: number): void {
    if (confirm('Are you sure you want to delete this appointment?')) {
      this.appointmentService.deleteAppointment(id).subscribe({
        next: () => {
          this.loadAppointments();
        },
        error: (error) => {
          console.error('Error deleting appointment', error);
        }
      });
    }
  }
  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadAppointments();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadAppointments();
    }
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}
