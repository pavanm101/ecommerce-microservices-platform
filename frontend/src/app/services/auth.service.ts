import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api';
  private tokenKey = 'ecommerce_token';
  private userSubject = new BehaviorSubject<any>(null);
  public user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    this.loadToken();
  }

  loadToken() {
    const token = localStorage.getItem(this.tokenKey);
    if (token) {
      // Decode JWT or validate - for now just assume logged in if token exists
      // In real app, verify exp claim
      this.userSubject.next({ token });
    }
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/auth/login`, { username, password }).pipe(
      tap(res => {
        if (res.access_token) {
          localStorage.setItem(this.tokenKey, res.access_token);
          this.userSubject.next({ token: res.access_token, username });
        }
      })
    );
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
