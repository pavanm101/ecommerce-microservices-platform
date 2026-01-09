import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <nav class="navbar">
      <div class="container">
        <a routerLink="/" class="logo">
          <span class="logo-icon">🛍️</span> LuxeStore
        </a>
        
        <div class="nav-links">
          <a routerLink="/products" routerLinkActive="active">Products</a>
          <a routerLink="/dashboard" *ngIf="isLoggedIn" routerLinkActive="active">Dashboard</a>
        </div>

        <div class="nav-actions">
          <a routerLink="/cart" class="cart-btn">
            🛒 <span class="badge" *ngIf="(cartCount$ | async) as count">{{count.length}}</span>
          </a>
          
          <ng-container *ngIf="isLoggedIn; else loginBtn">
            <button (click)="logout()" class="btn-logout">Logout</button>
          </ng-container>
          <ng-template #loginBtn>
            <a routerLink="/login" class="btn-login">Login</a>
          </ng-template>
        </div>
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(10px);
      border-bottom: 1px solid rgba(0,0,0,0.05);
      padding: 1rem 0;
      position: sticky;
      top: 0;
      z-index: 1000;
      
      .container {
        max-width: 1200px;
        margin: 0 auto;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 20px;
      }

      .logo {
        font-size: 1.5rem;
        font-weight: 700;
        color: #1a1a1a;
        text-decoration: none;
        display: flex;
        align-items: center;
        gap: 0.5rem;
        background: linear-gradient(135deg, #6366f1 0%, #a855f7 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        
        .logo-icon {
          -webkit-text-fill-color: initial;
        }
      }

      .nav-links {
        display: flex;
        gap: 2rem;
        
        a {
          text-decoration: none;
          color: #64748b;
          font-weight: 500;
          transition: color 0.3s;
          
          &:hover, &.active {
            color: #6366f1;
          }
        }
      }

      .nav-actions {
        display: flex;
        align-items: center;
        gap: 1.5rem;
      }

      .cart-btn {
        text-decoration: none;
        position: relative;
        font-size: 1.2rem;
        
        .badge {
          position: absolute;
          top: -8px;
          right: -8px;
          background: #ef4444;
          color: white;
          font-size: 0.7rem;
          padding: 2px 6px;
          border-radius: 10px;
          font-weight: bold;
        }
      }

      .btn-login {
        text-decoration: none;
        color: #6366f1;
        font-weight: 600;
        padding: 0.5rem 1.5rem;
        border: 1px solid #6366f1;
        border-radius: 8px;
        transition: all 0.3s;
        
        &:hover {
          background: #6366f1;
          color: white;
        }
      }
      
      .btn-logout {
        background: none;
        border: none;
        color: #64748b;
        cursor: pointer;
        font-weight: 500;
        
        &:hover {
          color: #ef4444;
        }
      }
    }
  `]
})
export class NavbarComponent {
  cartCount$;
  isLoggedIn = false;

  constructor(public auth: AuthService, public cart: CartService) {
    this.cartCount$ = cart.cart$;
    auth.user$.subscribe(user => this.isLoggedIn = !!user);
  }

  logout() {
    this.auth.logout();
  }
}
