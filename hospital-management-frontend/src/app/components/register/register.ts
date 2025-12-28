import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register {
  user = {
    username: '',
    password: '',
    email: ''
  };
  errorMessage = '';
  successMessage = '';

  constructor(
    private authService: Auth,
    private router: Router
  ) {}

  register(): void {
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
        this.errorMessage = error.error || 'Registration failed';
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}