import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';

@Component({
    selector: 'app-payment-success',
    standalone: true,
    imports: [CommonModule, RouterModule],
    template: `
    <div class="success-page">
      <div class="card">
        <div class="icon">✅</div>
        <h2>Payment Successful!</h2>
        <p>Thank you for your purchase. Your order has been confirmed.</p>
        <p class="session-id" *ngIf="sessionId">Session ID: {{sessionId}}</p>
        <a routerLink="/products" class="btn-home">Back to Shopping</a>
      </div>
    </div>
  `,
    styles: [`
    .success-page {
      min-height: 80vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f8fafc;
    }

    .card {
      background: white;
      padding: 3rem;
      border-radius: 16px;
      text-align: center;
      box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
      max-width: 500px;
    }

    .icon {
      font-size: 4rem;
      margin-bottom: 1.5rem;
    }

    h2 {
      color: #0f172a;
      margin-bottom: 1rem;
    }

    p {
      color: #64748b;
      margin-bottom: 2rem;
    }

    .session-id {
      font-size: 0.8rem;
      background: #f1f5f9;
      padding: 0.5rem;
      border-radius: 4px;
      font-family: monospace;
    }

    .btn-home {
      display: inline-block;
      padding: 0.75rem 1.5rem;
      background: #10b981;
      color: white;
      text-decoration: none;
      border-radius: 8px;
      font-weight: 600;
      transition: background 0.2s;
      
      &:hover { background: #059669; }
    }
  `]
})
export class PaymentSuccessComponent implements OnInit {
    sessionId: string | null = null;

    constructor(private route: ActivatedRoute) { }

    ngOnInit() {
        this.sessionId = this.route.snapshot.queryParamMap.get('session_id');
    }
}
