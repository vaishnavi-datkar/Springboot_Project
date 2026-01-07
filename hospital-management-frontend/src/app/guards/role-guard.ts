import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { inject } from '@angular/core';

export const roleGuard: CanActivateFn = (route, state) => {
  return true;
};
// Only ADMIN can access
export const adminGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  if (authService.getUserRole() === 'ADMIN') return true;
  
  router.navigate(['/dashboard']);
  return false;
};

// DOCTOR or ADMIN can access
export const doctorGuard = () => {
  const authService = inject(AuthService);
  const role = authService.getUserRole();
  
  return role === 'DOCTOR' || role === 'ADMIN';
};

// PATIENT or ADMIN can access
export const patientGuard = () => {
  const authService = inject(AuthService);
  const role = authService.getUserRole();
  
  return role === 'PATIENT' || role === 'ADMIN';
};
