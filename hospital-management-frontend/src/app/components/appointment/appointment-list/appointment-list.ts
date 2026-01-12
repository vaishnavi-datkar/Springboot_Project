import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Appointment } from '../../../interface/appointment';
import { appointmentService } from '../../../services/appointment';
import { AuthService } from '../../../services/auth';

@Component({ selector: 'app-appointment-list',
imports: [CommonModule, FormsModule], 
templateUrl: './appointment-list.html', 
styleUrl: './appointment-list.css', 
})
export class AppointmentList implements OnInit {
  appointments: Appointment[] = [];

  currentPage = 0;
  pageSize = 10;
  totalPages = 0;

  constructor(
    private appointmentService: appointmentService,
     private authService: AuthService,  
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAppointments();
  }

  loadAppointments(): void {
    this.appointmentService
      .getAllAppointments(this.currentPage, this.pageSize)
      .subscribe({
        next: (response: any) => {
          this.appointments = response.content;
          this.totalPages = response.totalPages;
        },
        error: (err) => {
          console.error('Error loading appointments', err);
        },
      });
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

  openAddModal(): void {
    this.router.navigate(['/appointments/new']);
  }

  openEditModal(appointment: Appointment): void {
    this.router.navigate(['/appointments/edit', appointment.id]);
  }

  deleteAppointment(id: number): void {
    if (confirm('Are you sure you want to delete this appointment?')) {
      this.appointmentService.deleteAppointment(id).subscribe({
        next: () => this.loadAppointments(),
        error: (err) => console.error('Delete failed', err),
      });
    }
  }

   openPrescription(appointment: Appointment): void {
    const role = this.authService.getUserRole();
    
    if (role === 'DOCTOR') {
      // Doctor opens form to write prescription
      this.router.navigate(['/prescriptions/form', appointment.id]);
    } else if (role === 'PATIENT') {
      // Patient views their prescription
      this.router.navigate(['/prescriptions/view', appointment.id]);
    } else {
      // ADMIN can also view
      this.router.navigate(['/prescriptions/view', appointment.id]);
    }
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}