package com.pavan.ecommerce.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private List<PaymentItem> items;
    private Long orderId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentItem {
        private String name;
        private BigDecimal price; // Unit price
        private int quantity;
        private Long productId;
    }
}
