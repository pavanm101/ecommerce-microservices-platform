package com.pavan.ecommerce.controllers;

import com.pavan.ecommerce.analytics.AnalyticsDashboard;
import com.pavan.ecommerce.analytics.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for analytics operations.
 * Provides dashboard metrics, sales trends, and business insights.
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Business analytics and insights APIs")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get analytics dashboard", description = "Returns comprehensive analytics with product metrics, category breakdowns, and price distributions")
    public ResponseEntity<AnalyticsDashboard> getDashboard() {
        AnalyticsDashboard dashboard = analyticsService.getDashboard();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/sales-trends")
    @Operation(summary = "Get sales trends", description = "Returns sales trends over time periods")
    public ResponseEntity<List<AnalyticsService.SalesTrend>> getSalesTrends(
            @Parameter(description = "Time interval (day, week, month)") @RequestParam(defaultValue = "month") String interval) {
        List<AnalyticsService.SalesTrend> trends = analyticsService.getSalesTrends(interval);
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/top-products")
    @Operation(summary = "Get top performing products", description = "Returns top products by sales or views")
    public ResponseEntity<List<TopProduct>> getTopProducts(
            @Parameter(description = "Number of top products to return") @RequestParam(defaultValue = "10") Integer limit,

            @Parameter(description = "Sort by: sales, views, revenue") @RequestParam(defaultValue = "sales") String sortBy) {
        // Placeholder - would aggregate from order events
        List<TopProduct> topProducts = List.of(
                new TopProduct(1L, "Product A", 150, 12500.00),
                new TopProduct(2L, "Product B", 120, 9800.00),
                new TopProduct(3L, "Product C", 95, 7600.00));
        return ResponseEntity.ok(topProducts);
    }

    public record TopProduct(Long productId, String productName, int salesCount, double revenue) {
    }
}
