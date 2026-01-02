import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard implements OnInit {
  username: string = '';
  userRole: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.username = localStorage.getItem('username') || 'User';
    this.userRole = localStorage.getItem('role') || '';
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }
  
  canAccessDoctors(): boolean {
    return this.userRole === 'DOCTOR' || this.userRole === 'ADMIN';
  }
  
  canAccessPatients(): boolean {
    return this.userRole === 'PATIENT' || this.userRole === 'DOCTOR' || this.userRole === 'ADMIN';
  }
  
  canAccessAppointments(): boolean {
    return this.userRole === 'ADMIN';
  }
}