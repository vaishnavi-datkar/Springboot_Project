import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  credentials = {
    username: '',
    password: ''
  };
  errorMessage = '';

  constructor(
    private authService: Auth,
    private router: Router
  ) {}

  login(): void {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        console.log('Login successful', response);
        localStorage.setItem('username', this.credentials.username); // Add this line
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        console.error('Login failed', error);
        this.errorMessage = 'Invalid username or password';
      }
    });
  }

  goToRegister(): void {
    this.router.navigate(['/register']);
  }
}