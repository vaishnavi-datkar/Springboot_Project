import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Register } from './components/register/register';
import { Dashboard } from './components/dashboard/dashboard';
import { DoctorList } from './components/doctor/doctor-list/doctor-list';
import { authGuard } from './guards/auth-guard';
import { DoctorForm } from './components/doctor/doctor-form/doctor-form';
import { AppointmentForm } from './components/appointment/appointment-form/appointment-form';
import { AppointmentList } from './components/appointment/appointment-list/appointment-list';
import { PatientForm } from './components/patient/patient-form/patient-form';
import { PatientList } from './components/patient/patient-list/patient-list';
import { PrescriptionList } from './components/prescription/prescription-list/prescription-list';
import { Prescriptionform } from './components/prescription/prescriptionform/prescriptionform';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'register', component: Register},
  { 
    path: 'dashboard', 
    component: Dashboard,
    canActivate: [authGuard]
  },
  { 
    path: 'doctors', 
    component: DoctorList,
    canActivate: [authGuard]
  },
  { 
    path: 'doctors/new', 
    component: DoctorForm,
    canActivate: [authGuard]
  },
  { 
    path: 'doctors/edit/:id', 
    component: DoctorForm,
    canActivate: [authGuard]
  },
  { 
    path: 'patients', 
    component: PatientList,
    canActivate: [authGuard]
    },
  { 
    path: 'patients/new', 
    component: PatientForm,
    canActivate: [authGuard]
  },
  { 
    path: 'patients/edit/:id', 
    component: PatientForm,
    canActivate: [authGuard]
  },
  { 
    path: 'appointments', 
    component: AppointmentList,
    canActivate: [authGuard]
  },
  { 
    path: 'appointments/new', 
    component: AppointmentForm,
    canActivate: [authGuard]
  },
  { 
    path: 'appointments/edit/:id', 
    component: AppointmentForm,
    canActivate: [authGuard]
    },
  { 
    path: 'prescriptions', 
    component: Dashboard, // Add PrescriptionList later
    canActivate: [authGuard]
  },
  { 
    path: 'prescriptions', 
    component: PrescriptionList,
    canActivate: [authGuard]
  },
   { 
    path: 'prescriptions/form/:id', 
    component: Prescriptionform 
  },
  { path: 'prescriptions/view/:id', component: Prescriptionform } 
];