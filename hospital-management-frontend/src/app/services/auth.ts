import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  private baseUrl = 'http://localhost:8091/api/auth';

  constructor(private http: HttpClient) {}

  register(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, user);
  }

  login(credentials: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials).pipe(
      tap((response: any) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('username', response.username);
        localStorage.setItem('role', response.role);
      })
    );
  }

  logout(): void {
  localStorage.removeItem('token');
  localStorage.removeItem('username');
  localStorage.removeItem('role');
  localStorage.clear(); // Clear everything
}

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
  
  getRole(): string | null {
    return localStorage.getItem('role');
  }
  
  getUsername(): string | null {
    return localStorage.getItem('username');
  }
}