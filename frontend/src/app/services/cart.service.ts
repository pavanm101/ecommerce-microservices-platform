import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export interface CartItem {
  id?: number;
  product: any;
  quantity: number;
  price: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private apiUrl = '/api/cart';
  private cartSubject = new BehaviorSubject<CartItem[]>([]);
  public cart$ = this.cartSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadCart();
  }

  loadCart() {
    this.http.get<CartItem[]>(this.apiUrl).subscribe(
      items => this.cartSubject.next(items),
      err => console.error('Failed to load cart', err)
    );
  }

  addToCart(productId: number, quantity: number = 1) {
    return this.http.post(`${this.apiUrl}/${productId}/1`, {}).subscribe(() => {
      this.loadCart(); // Reload cart after add
    });
  }

  removeFromCart(productId: number) {
    return this.http.delete(`${this.apiUrl}/${productId}`).subscribe(() => {
      this.loadCart();
    });
  }

  checkout(orderData: any) {
    const items = this.cartSubject.value.map(item => ({
      name: item.product.name,
      price: item.price,
      quantity: item.quantity,
      productId: item.product.id
    }));

    return this.http.post<any>('/api/payment/checkout', { items, orderId: Date.now() })
      .subscribe(res => {
        if (res.paymentUrl) {
          window.location.href = res.paymentUrl;
        }
      });
  }

  getCartTotal(): number {
    return this.cartSubject.value.reduce((acc, item) => acc + (item.price * item.quantity), 0);
  }
}
