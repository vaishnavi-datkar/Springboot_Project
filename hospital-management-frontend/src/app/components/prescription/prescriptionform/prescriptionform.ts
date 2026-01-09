import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth';
import { PrescriptionService } from '../../../services/prescriptionservice';
import { appointmentService } from '../../../services/appointment';

@Component({
  selector: 'app-prescriptionform',
  imports: [],
  templateUrl: './prescriptionform.html',
  styleUrl: './prescriptionform.css',
})
export class Prescriptionform implements OnInit {
  appointmentId: number = 0;
  prescription: any = {
    appointmentId: 0,
    pName: '',
    dName: '',
    medicine: '',
    diagnosis: '',
    notes: ''
  };
  
  isViewMode = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private prescriptionService: PrescriptionService,
    private appointmentService: appointmentService,
    private authService: AuthService
  ) {}
 ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.appointmentId = +params['id'];
      this.prescription.appointmentId = this.appointmentId;
      
      const role = this.authService.getUserRole();
      this.isViewMode = role === 'PATIENT';
      
      // Load appointment details
      this.loadAppointmentDetails();
      
      // Check if prescription already exists
      this.loadExistingPrescription();
    });
  }

  loadAppointmentDetails(): void {
    this.appointmentService.getAppointmentById(this.appointmentId).subscribe({
      next: (appointment: any) => {
        this.prescription.pName = appointment.patientName;
        this.prescription.dName = appointment.doctorName;
      },
      error: (err) => {
        console.error('Error loading appointment', err);
      }
    });
  }

  loadExistingPrescription(): void {
    this.prescriptionService.getPrescriptionByAppointment(this.appointmentId).subscribe({
      next: (data: any) => {
        if (data) {
          this.prescription = data;
        }
      },
      error: (err) => {
        console.log('No existing prescription');
      }
    });
  }

  savePrescription(): void {
    if (!this.prescription.medicine || !this.prescription.diagnosis) {
      this.errorMessage = 'Please fill all required fields';
      return;
    }
    if (this.prescription.id) {
      // Update existing
      this.prescriptionService.updatePrescription(this.prescription.id, this.prescription).subscribe({
        next: () => {
          alert('Prescription updated successfully');
          this.router.navigate(['/appointments']);
        },
        error: (err) => {
          this.errorMessage = 'Failed to update prescription';
        }
      });
    } else {
      // Create new
      this.prescriptionService.createPrescription(this.prescription).subscribe({
        next: () => {
          alert('Prescription created successfully');
          this.router.navigate(['/appointments']);
        },
        error: (err) => {
          this.errorMessage = 'Failed to create prescription';
        }
      });
    }
  }
  cancel(): void {
    this.router.navigate(['/appointments']);
  }

}
