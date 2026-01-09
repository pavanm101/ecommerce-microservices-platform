import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService, CartItem } from '../../services/cart.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="cart-page container">
      <h2>Shopping Cart</h2>
      
      <div class="cart-container" *ngIf="(cartService.cart$ | async) as cartItems">
        <div class="cart-items" *ngIf="cartItems.length > 0; else emptyCart">
          <div *ngFor="let item of cartItems" class="cart-item">
            <div class="item-img">{{item.product.name.charAt(0)}}</div>
            <div class="item-details">
              <h3>{{item.product.name}}</h3>
              <p>\${{item.price}}</p>
            </div>
            <div class="item-actions">
              <input type="number" [value]="item.quantity" min="1" readonly>
              <button (click)="removeItem(item.product.id)" class="btn-remove">Remove</button>
            </div>
            <div class="item-subtotal">
              \${{item.price * item.quantity}}
            </div>
          </div>
        </div>

        <ng-template #emptyCart>
          <div class="empty-state">
            <p>Your cart is empty</p>
            <a routerLink="/products" class="btn-shop">Continue Shopping</a>
          </div>
        </ng-template>

        <div class="cart-summary" *ngIf="cartItems.length > 0">
          <h3>Order Summary</h3>
          <div class="summary-row">
            <span>Subtotal</span>
            <span>\${{cartService.getCartTotal()}}</span>
          </div>
          <div class="summary-row total">
            <span>Total</span>
            <span>\${{cartService.getCartTotal()}}</span>
          </div>
          <button (click)="checkout()" class="btn-checkout">Proceed to Checkout</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .cart-page {
      padding: 2rem 0;
      max-width: 1000px;
      margin: 0 auto;
    }

    .cart-container {
      display: grid;
      grid-template-columns: 1fr 300px;
      gap: 2rem;
      
      @media (max-width: 768px) {
        grid-template-columns: 1fr;
      }
    }

    .cart-items {
      background: white;
      border-radius: 12px;
      padding: 1.5rem;
      box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
    }

    .cart-item {
      display: flex;
      align-items: center;
      padding: 1rem 0;
      border-bottom: 1px solid #f1f5f9;
      gap: 1.5rem;
      
      &:last-child { border-bottom: none; }

      .item-img {
        width: 60px;
        height: 60px;
        background: #f1f5f9;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: bold;
        color: #94a3b8;
      }

      .item-details {
        flex: 1;
        
        h3 { margin: 0; font-size: 1rem; }
        p { margin: 0.25rem 0 0; color: #64748b; }
      }

      .item-actions {
        display: flex;
        align-items: center;
        gap: 1rem;
        
        input {
          width: 50px;
          padding: 0.25rem;
          border: 1px solid #cbd5e1;
          border-radius: 4px;
          text-align: center;
        }

        .btn-remove {
          color: #ef4444;
          background: none;
          border: none;
          cursor: pointer;
          font-size: 0.9rem;
          
          &:hover { text-decoration: underline; }
        }
      }

      .item-subtotal {
        font-weight: 600;
        font-size: 1.1rem;
        width: 80px;
        text-align: right;
      }
    }

    .cart-summary {
      background: white;
      border-radius: 12px;
      padding: 1.5rem;
      height: fit-content;
      box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);

      h3 { margin-top: 0; margin-bottom: 1.5rem; }

      .summary-row {
        display: flex;
        justify-content: space-between;
        margin-bottom: 1rem;
        color: #64748b;
        
        &.total {
          border-top: 1px solid #f1f5f9;
          padding-top: 1rem;
          font-weight: 700;
          color: #0f172a;
          font-size: 1.2rem;
        }
      }

      .btn-checkout {
        width: 100%;
        padding: 0.75rem;
        background: #6366f1;
        color: white;
        border: none;
        border-radius: 8px;
        font-weight: 600;
        margin-top: 1rem;
        cursor: pointer;
        transition: background 0.2s;
        
        &:hover { background: #4f46e5; }
      }
    }

    .empty-state {
      text-align: center;
      padding: 3rem;
      
      .btn-shop {
        display: inline-block;
        margin-top: 1rem;
        padding: 0.75rem 1.5rem;
        background: #6366f1;
        color: white;
        text-decoration: none;
        border-radius: 8px;
      }
    }
  `]
})
export class CartComponent {
  constructor(public cartService: CartService) { }

  removeItem(productId: number) {
    this.cartService.removeFromCart(productId);
  }

  checkout() {
    this.cartService.checkout({});
  }
}
