import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Prescription } from '../interface/prescription';
@Injectable({
  providedIn: 'root',
})
export class PrescriptionService {
 
  private baseUrl = 'http://localhost:8091/api/prescriptions';

  constructor(private http: HttpClient) {}

  getAllPrescriptions(): Observable<Prescription[]> {
    return this.http.get<Prescription[]>(this.baseUrl);
  }

  getPrescriptionById(id: number): Observable<Prescription> {
    return this.http.get<Prescription>(`${this.baseUrl}/${id}`);
  }

  createPrescription(prescription: Prescription): Observable<Prescription> {
    return this.http.post<Prescription>(this.baseUrl, prescription);
  }

  updatePrescription(id: number, prescription: Prescription): Observable<Prescription> {
    return this.http.put<Prescription>(`${this.baseUrl}/${id}`, prescription);
  }

  deletePrescription(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
