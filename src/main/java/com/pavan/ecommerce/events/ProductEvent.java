package com.pavan.ecommerce.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product event for Kafka streaming.
 * Published when products are created, updated, or deleted.
 * Used to sync product data to Elasticsearch.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEvent {

    private Long productId;
    private String eventType; // CREATED, UPDATED, DELETED
    private LocalDateTime eventTime;
    private String productName;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private String categoryName;
    private Integer stockQuantity;
    private Boolean inStock;

    // Event type constants
    public static final String PRODUCT_CREATED = "PRODUCT_CREATED";
    public static final String PRODUCT_UPDATED = "PRODUCT_UPDATED";
    public static final String PRODUCT_DELETED = "PRODUCT_DELETED";
}
