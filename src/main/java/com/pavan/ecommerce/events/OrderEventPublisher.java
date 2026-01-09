package com.pavan.ecommerce.events;

import com.pavan.ecommerce.config.KafkaTopicConfig;
import com.pavan.ecommerce.models.Order;
import com.pavan.ecommerce.models.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Kafka producer for order events.
 * Publishes order lifecycle events to Kafka topic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    /**
     * Publish order created event
     */
    public void publishOrderCreated(Order order) {
        OrderEvent event = buildOrderEvent(order, OrderEvent.ORDER_CREATED);
        publishEvent(event);
    }

    /**
     * Publish order paid event
     */
    public void publishOrderPaid(Order order) {
        OrderEvent event = buildOrderEvent(order, OrderEvent.ORDER_PAID);
        publishEvent(event);
    }

    /**
     * Publish order shipped event
     */
    public void publishOrderShipped(Order order) {
        OrderEvent event = buildOrderEvent(order, OrderEvent.ORDER_SHIPPED);
        publishEvent(event);
    }

    /**
     * Publish order delivered event
     */
    public void publishOrderDelivered(Order order) {
        OrderEvent event = buildOrderEvent(order, OrderEvent.ORDER_DELIVERED);
        publishEvent(event);
    }

    /**
     * Publish order cancelled event
     */
    public void publishOrderCancelled(Order order) {
        OrderEvent event = buildOrderEvent(order, OrderEvent.ORDER_CANCELLED);
        publishEvent(event);
    }

    /**
     * Build OrderEvent from Order entity
     */
    private OrderEvent buildOrderEvent(Order order, String eventType) {
        List<OrderEvent.OrderItemEvent> itemEvents = List.of();

        if (order.getItems() != null) {
            itemEvents = order.getItems().stream()
                    .map(item -> OrderEvent.OrderItemEvent.builder()
                            .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                            .productName(item.getProduct() != null ? item.getProduct().getName() : null)
                            .quantity(1) // Default quantity since not available in OrderItem
                            .unitPrice(item.getPrice() != null ? new BigDecimal(item.getPrice()) : null)
                            .subtotal(item.getPrice() != null ? new BigDecimal(item.getPrice()) : null)
                            .build())
                    .collect(Collectors.toList());
        }

        return OrderEvent.builder()
                .orderId(order.getId())
                .eventType(eventType)
                .eventTime(LocalDateTime.now())
                .customerName(order.getName())
                .totalAmount(order.getTotalPrice() != null ? new BigDecimal(order.getTotalPrice()) : null)
                .status(order.getStatus())
                .items(itemEvents)
                .shippingInfo(OrderEvent.ShippingInfo.builder()
                        .address(order.getAddress())
                        .city(order.getCity())
                        .zipCode(order.getZip())
                        .build())
                .build();
    }

    /**
     * Publish event to Kafka
     */
    private void publishEvent(OrderEvent event) {
        String key = String.valueOf(event.getOrderId());

        CompletableFuture<SendResult<String, OrderEvent>> future = kafkaTemplate
                .send(KafkaTopicConfig.ORDER_EVENTS_TOPIC, key, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Published order event: {} for order {} to partition {}",
                        event.getEventType(),
                        event.getOrderId(),
                        result.getRecordMetadata().partition());
            } else {
                log.error("Failed to publish order event: {} for order {}",
                        event.getEventType(),
                        event.getOrderId(),
                        ex);
            }
        });
    }
}
