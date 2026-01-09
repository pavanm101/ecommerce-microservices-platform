package com.pavan.ecommerce.analytics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import com.pavan.ecommerce.search.ProductDocument;
import com.pavan.ecommerce.search.ProductSearchRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Analytics service for business insights.
 * Uses Elasticsearch for product analytics.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ProductSearchRepository productSearchRepository;

    /**
     * Get analytics dashboard with metrics
     */
    public AnalyticsDashboard getDashboard() {
        long totalProducts = productSearchRepository.count();

        // Get in-stock products
        List<ProductDocument> inStockProducts = productSearchRepository.findByInStockTrue();
        long inStockCount = inStockProducts.size();

        // Calculate price statistics
        BigDecimal avgPrice = BigDecimal.ZERO;
        BigDecimal minPrice = BigDecimal.ZERO;
        BigDecimal maxPrice = BigDecimal.ZERO;

        Iterable<ProductDocument> allProducts = productSearchRepository.findAll();
        List<BigDecimal> prices = new ArrayList<>();

        for (ProductDocument product : allProducts) {
            if (product.getPrice() != null) {
                prices.add(product.getPrice());
            }
        }

        if (!prices.isEmpty()) {
            minPrice = prices.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            maxPrice = prices.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            BigDecimal sum = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            avgPrice = sum.divide(BigDecimal.valueOf(prices.size()), 2, java.math.RoundingMode.HALF_UP);
        }

        return AnalyticsDashboard.builder()
                .totalProducts(totalProducts)
                .inStockProducts(inStockCount)
                .outOfStockProducts(totalProducts - inStockCount)
                .averagePrice(avgPrice)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .topCategories(new ArrayList<>())
                .priceDistribution(new ArrayList<>())
                .build();
    }

    /**
     * Get sales trends (placeholder - would integrate with order data)
     */
    public List<SalesTrend> getSalesTrends(String interval) {
        return List.of(
                SalesTrend.builder().period("2024-01").orderCount(120).revenue(BigDecimal.valueOf(15000)).build(),
                SalesTrend.builder().period("2024-02").orderCount(150).revenue(BigDecimal.valueOf(18500)).build(),
                SalesTrend.builder().period("2024-03").orderCount(180).revenue(BigDecimal.valueOf(22000)).build());
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class SalesTrend {
        private String period;
        private long orderCount;
        private BigDecimal revenue;
    }
}
