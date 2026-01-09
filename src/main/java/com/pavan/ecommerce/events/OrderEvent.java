package com.pavan.ecommerce.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order event for Kafka streaming.
 * Published when order state changes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

    private Long orderId;
    private String eventType; // CREATED, PAID, SHIPPED, DELIVERED, CANCELLED
    private LocalDateTime eventTime;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private BigDecimal totalAmount;
    private String status;
    private List<OrderItemEvent> items;
    private ShippingInfo shippingInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemEvent {
        private Long productId;
        private String productName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingInfo {
        private String address;
        private String city;
        private String zipCode;
        private String country;
    }

    // Event type constants
    public static final String ORDER_CREATED = "ORDER_CREATED";
    public static final String ORDER_PAID = "ORDER_PAID";
    public static final String ORDER_SHIPPED = "ORDER_SHIPPED";
    public static final String ORDER_DELIVERED = "ORDER_DELIVERED";
    public static final String ORDER_CANCELLED = "ORDER_CANCELLED";
}
