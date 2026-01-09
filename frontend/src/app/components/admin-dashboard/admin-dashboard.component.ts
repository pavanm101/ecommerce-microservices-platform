import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="dashboard container">
      <header class="header">
        <h2>Analytics Dashboard</h2>
        <div class="period-selector">
          <select>
            <option>Last 30 Days</option>
            <option>Last 7 Days</option>
            <option>Today</option>
          </select>
        </div>
      </header>

      <div class="metrics-grid" *ngIf="dashboard">
        <div class="metric-card">
          <h3>Total Products</h3>
          <div class="value">{{dashboard.totalProducts}}</div>
        </div>
        <div class="metric-card">
          <h3>In Stock</h3>
          <div class="value success">{{dashboard.inStockProducts}}</div>
        </div>
        <div class="metric-card">
          <h3>Avg Price</h3>
          <div class="value">\${{dashboard.averagePrice}}</div>
        </div>
        <div class="metric-card">
          <h3>Top Category</h3>
          <div class="value highlight">
            {{dashboard.topCategories[0]?.categoryName || 'N/A'}}
          </div>
        </div>
      </div>

      <div class="charts-grid">
        <div class="chart-card">
          <h3>Price Distribution</h3>
          <div class="bars">
            <div *ngFor="let item of dashboard?.priceDistribution" class="bar-row">
              <span class="label">{{item.range}}</span>
              <div class="bar-container">
                <div class="bar" [style.width.%]="(item.count / 50) * 100"></div>
              </div>
              <span class="count">{{item.count}}</span>
            </div>
          </div>
        </div>
        
        <div class="chart-card">
          <h3>Top Categories</h3>
          <div class="bars">
            <div *ngFor="let item of dashboard?.topCategories" class="bar-row">
              <span class="label">{{item.categoryName}}</span>
              <div class="bar-container">
                <div class="bar secondary" [style.width.%]="(item.productCount / 50) * 100"></div>
              </div>
              <span class="count">{{item.productCount}}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard {
      padding: 2rem 0;
      max-width: 1200px;
      margin: 0 auto;
    }

    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 2rem;
      
      h2 { margin: 0; }
    }

    .metrics-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1.5rem;
      margin-bottom: 2rem;
    }

    .metric-card {
      background: white;
      padding: 1.5rem;
      border-radius: 12px;
      box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);

      h3 {
        margin: 0 0 0.5rem 0;
        font-size: 0.9rem;
        color: #64748b;
        font-weight: 500;
      }

      .value {
        font-size: 2rem;
        font-weight: 700;
        color: #0f172a;
        
        &.success { color: #10b981; }
        &.highlight { color: #6366f1; font-size: 1.5rem; }
      }
    }

    .charts-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
      gap: 2rem;
    }

    .chart-card {
      background: white;
      padding: 1.5rem;
      border-radius: 12px;
      box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);

      h3 { margin-top: 0; margin-bottom: 1.5rem; }
    }

    .bar-row {
      display: flex;
      align-items: center;
      margin-bottom: 0.75rem;
      font-size: 0.9rem;
      
      .label { width: 100px; color: #64748b; }
      
      .bar-container {
        flex: 1;
        background: #f1f5f9;
        height: 8px;
        border-radius: 4px;
        margin: 0 1rem;
        overflow: hidden;
        
        .bar {
          height: 100%;
          background: #6366f1;
          border-radius: 4px;
          
          &.secondary { background: #ec4899; }
        }
      }
      
      .count { width: 30px; text-align: right; font-weight: 600; }
    }
  `]
})
export class AdminDashboardComponent implements OnInit {
  dashboard: any;

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.http.get<any>('/api/analytics/dashboard').subscribe(
      data => this.dashboard = data
    );
  }
}
