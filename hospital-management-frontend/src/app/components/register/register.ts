import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

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
    private authService: Auth,
    private router: Router
  ) {}

  register(): void {
    // Validate role selection
    if (!this.user.role) {
      this.errorMessage = 'Please select your role';
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    this.authService.register(this.user).subscribe({
      next: (response) => {
        console.log('Registration successful', response);
        this.successMessage = 'Registration successful! Redirecting to login...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (error) => {
        console.error('Registration failed', error);
        
        // Properly extract error message
        if (error.error) {
          if (typeof error.error === 'string') {
            this.errorMessage = error.error;
          } else if (error.error.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = 'Registration failed. Please try again.';
          }
        } else if (error.message) {
          this.errorMessage = error.message;
        } else {
          this.errorMessage = 'Registration failed. Please check your network connection.';
        }
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}