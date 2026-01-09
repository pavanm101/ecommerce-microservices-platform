package com.pavan.ecommerce.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Analytics dashboard response with key metrics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDashboard {

    private long totalProducts;
    private long inStockProducts;
    private long outOfStockProducts;
    private BigDecimal averagePrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<CategoryMetric> topCategories;
    private List<PriceRangeBucket> priceDistribution;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryMetric {
        private Long categoryId;
        private String categoryName;
        private long productCount;
        private BigDecimal averagePrice;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceRangeBucket {
        private String range;
        private BigDecimal from;
        private BigDecimal to;
        private long count;
    }
}
