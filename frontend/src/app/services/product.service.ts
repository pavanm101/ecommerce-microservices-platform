import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  group: { id: number; groupName: string };
  stockQuantity: number;
  inStock: boolean;
}

export interface SearchResult {
  products: Product[];
  totalHits: number;
  page: number;
  size: number;
  facets: any;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) { }

  getProducts(page = 0, size = 20): Observable<Product[]> {
    // Fallback to basic list if search not used
    return this.http.get<Product[]>(`${this.apiUrl}/products`);
  }

  searchProducts(query: string, filters: any = {}): Observable<SearchResult> {
    let params = new HttpParams()
      .set('q', query || '')
      .set('page', filters.page || 0)
      .set('size', filters.size || 20);

    if (filters.categoryId) params = params.set('categoryId', filters.categoryId);
    if (filters.minPrice) params = params.set('minPrice', filters.minPrice);
    if (filters.maxPrice) params = params.set('maxPrice', filters.maxPrice);

    return this.http.get<SearchResult>(`${this.apiUrl}/search/products`, { params });
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/products/${id}`);
  }

  getCategories(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/groups`);
  }
}
