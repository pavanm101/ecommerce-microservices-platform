import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <h2>Welcome Back</h2>
        <p class="subtitle">Sign in to your account</p>
        
        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label>Username</label>
            <input type="text" formControlName="username" placeholder="johndoe">
          </div>
          
          <div class="form-group">
            <label>Password</label>
            <input type="password" formControlName="password" placeholder="••••••••">
          </div>
          
          <button type="submit" [disabled]="loginForm.invalid || loading" class="btn-submit">
            {{ loading ? 'Signing In...' : 'Sign In' }}
          </button>
        </form>
        
        <div class="footer">
          Don't have an account? <a routerLink="/register">Register</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      min-height: calc(100vh - 80px);
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f8fafc;
    }

    .login-card {
      background: white;
      padding: 2.5rem;
      border-radius: 16px;
      box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 400px;
      text-align: center;

      h2 {
        margin: 0;
        color: #0f172a;
        font-size: 1.8rem;
      }

      .subtitle {
        color: #64748b;
        margin-bottom: 2rem;
      }

      .form-group {
        text-align: left;
        margin-bottom: 1.5rem;
        
        label {
          display: block;
          margin-bottom: 0.5rem;
          color: #334155;
          font-weight: 500;
          font-size: 0.9rem;
        }

        input {
          width: 100%;
          padding: 0.75rem;
          border: 1px solid #cbd5e1;
          border-radius: 8px;
          transition: all 0.2s;
          
          &:focus {
            outline: none;
            border-color: #6366f1;
            box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
          }
        }
      }

      .btn-submit {
        width: 100%;
        padding: 0.75rem;
        background: #6366f1;
        color: white;
        border: none;
        border-radius: 8px;
        font-weight: 600;
        cursor: pointer;
        transition: background 0.2s;
        
        &:hover { background: #4f46e5; }
        &:disabled { background: #94a3b8; cursor: not-allowed; }
      }

      .footer {
        margin-top: 1.5rem;
        font-size: 0.9rem;
        color: #64748b;
        
        a {
          color: #6366f1;
          text-decoration: none;
          font-weight: 500;
          
          &:hover { text-decoration: underline; }
        }
      }
    }
  `]
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loading = true;
      const { username, password } = this.loginForm.value;
      this.auth.login(username, password).subscribe({
        next: () => {
          this.router.navigate(['/products']);
        },
        error: (err) => {
          console.error(err);
          this.loading = false;
          alert('Login failed');
        }
      });
    }
  }
}
