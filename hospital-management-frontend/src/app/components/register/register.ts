import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  user = {
    username: '',
    password: '',
    email: '',
    role: ''
  };
  errorMessage = '';
  successMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  register(): void {
  if (!this.user.role) {
    this.errorMessage = 'Please select your role';
    return;
  }

  this.errorMessage = '';
  this.successMessage = '';

  this.authService.register(this.user).subscribe({
    next: (response: any) => {
      console.log('Registration response:', response);
      
      // Handle both string and object responses
      if (typeof response === 'string') {
        this.successMessage = response;
      } else if (response.message) {
        this.successMessage = response.message;
      } else {
        this.successMessage = 'Registration successful!';
      }
      
      setTimeout(() => {
        this.router.navigate(['/login']);
      }, 2000);
    },
    error: (error) => {
      console.error('Registration error:', error);
      
      // Handle different error formats
      if (error.error) {
        if (typeof error.error === 'string') {
          this.errorMessage = error.error;
        } else if (error.error.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Registration failed. Please try again.';
        }
      } else {
        this.errorMessage = 'Registration failed. Please check your connection.';
      }
    }
  });
}

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}