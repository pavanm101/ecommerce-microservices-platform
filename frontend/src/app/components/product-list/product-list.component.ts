import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService, Product, SearchResult } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="product-page container">
      <aside class="filters">
        <h3>Filters</h3>
        <div class="filter-group">
          <label>Search</label>
          <input type="text" [(ngModel)]="searchQuery" (keyup.enter)="search()" placeholder="Search products...">
        </div>
        
        <div class="filter-group">
          <label>Price Range</label>
          <div class="price-inputs">
            <input type="number" [(ngModel)]="filters.minPrice" placeholder="Min">
            <span>-</span>
            <input type="number" [(ngModel)]="filters.maxPrice" placeholder="Max">
          </div>
        </div>

        <button (click)="search()" class="btn-apply">Apply Filters</button>
      </aside>

      <main class="products-grid">
        <div *ngFor="let product of products" class="product-card">
          <div class="product-image">
            <!-- Placeholder image for now -->
            <div class="placeholder-img">{{product.name.charAt(0)}}</div>
          </div>
          <div class="product-info">
            <span class="category">{{product.group?.groupName || 'General'}}</span>
            <h3>{{product.name}}</h3>
            <p class="description">{{product.description | slice:0:100}}...</p>
            <div class="price-action">
              <span class="price">\${{product.price}}</span>
              <button (click)="addToCart(product)" class="btn-add">
                Add to Cart
              </button>
            </div>
            <a [routerLink]="['/products', product.id]" class="details-link">View Details</a>
          </div>
        </div>
      </main>
    </div>
  `,
  styles: [`
    .product-page {
      display: grid;
      grid-template-columns: 250px 1fr;
      gap: 2rem;
      padding: 2rem 0;
      max-width: 1200px;
      margin: 0 auto;
    }

    .filters {
      background: white;
      padding: 1.5rem;
      border-radius: 12px;
      height: fit-content;
      position: sticky;
      top: 100px;
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);

      h3 { margin-top: 0; margin-bottom: 1.5rem; }

      .filter-group {
        margin-bottom: 1.5rem;
        
        label {
          display: block;
          margin-bottom: 0.5rem;
          font-weight: 500;
          color: #475569;
        }

        input {
          width: 100%;
          padding: 0.5rem;
          border: 1px solid #e2e8f0;
          border-radius: 6px;
          
          &:focus {
            outline: none;
            border-color: #6366f1;
            ring: 2px solid #e0e7ff;
          }
        }

        .price-inputs {
          display: flex;
          gap: 0.5rem;
          align-items: center;
        }
      }

      .btn-apply {
        width: 100%;
        padding: 0.75rem;
        background: #1e293b;
        color: white;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        transition: background 0.2s;
        
        &:hover { background: #0f172a; }
      }
    }

    .products-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 2rem;
    }

    .product-card {
      background: white;
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
      transition: transform 0.2s, box-shadow 0.2s;
      display: flex;
      flex-direction: column;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
      }

      .product-image {
        height: 200px;
        background: #f1f5f9;
        display: flex;
        align-items: center;
        justify-content: center;
        
        .placeholder-img {
          font-size: 4rem;
          font-weight: bold;
          color: #cbd5e1;
        }
      }

      .product-info {
        padding: 1.5rem;
        flex: 1;
        display: flex;
        flex-direction: column;

        .category {
          font-size: 0.8rem;
          color: #6366f1;
          font-weight: 600;
          text-transform: uppercase;
        }

        h3 {
          margin: 0.5rem 0;
          font-size: 1.25rem;
          color: #1e293b;
        }

        .description {
          color: #64748b;
          font-size: 0.9rem;
          margin-bottom: 1.5rem;
          flex-grow: 1;
        }

        .price-action {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-top: auto;
          
          .price {
            font-size: 1.5rem;
            font-weight: 700;
            color: #1e293b;
          }

          .btn-add {
            padding: 0.5rem 1rem;
            background: #6366f1;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            transition: background 0.2s;
            
            &:hover { background: #4f46e5; }
          }
        }
        
        .details-link {
            display: block;
            text-align: center;
            margin-top: 1rem;
            color: #64748b;
            text-decoration: none;
            font-size: 0.85rem;
            
            &:hover { text-decoration: underline; }
        }
      }
    }
  `]
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  searchQuery = '';
  filters: any = {};

  constructor(
    private productService: ProductService,
    private cartService: CartService
  ) { }

  ngOnInit() {
    this.search();
  }

  search() {
    this.productService.searchProducts(this.searchQuery, this.filters).subscribe(res => {
      this.products = res.products;
    });
  }

  addToCart(product: Product) {
    this.cartService.addToCart(product.id, 1);
  }
}
