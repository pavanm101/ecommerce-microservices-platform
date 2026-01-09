package com.pavan.ecommerce.events;

import com.pavan.ecommerce.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer for order events.
 * Processes order lifecycle events for analytics and notifications.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    @KafkaListener(topics = KafkaTopicConfig.ORDER_EVENTS_TOPIC, groupId = "order-processor-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderEvent(OrderEvent event) {
        log.info("Received order event: {} for order {}", event.getEventType(), event.getOrderId());

        try {
            switch (event.getEventType()) {
                case OrderEvent.ORDER_CREATED:
                    handleOrderCreated(event);
                    break;
                case OrderEvent.ORDER_PAID:
                    handleOrderPaid(event);
                    break;
                case OrderEvent.ORDER_SHIPPED:
                    handleOrderShipped(event);
                    break;
                case OrderEvent.ORDER_DELIVERED:
                    handleOrderDelivered(event);
                    break;
                case OrderEvent.ORDER_CANCELLED:
                    handleOrderCancelled(event);
                    break;
                default:
                    log.warn("Unknown order event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Error processing order event: {} for order {}",
                    event.getEventType(), event.getOrderId(), e);
        }
    }

    private void handleOrderCreated(OrderEvent event) {
        log.info("Processing ORDER_CREATED for order {}: customer={}, total={}",
                event.getOrderId(), event.getCustomerName(), event.getTotalAmount());
        // TODO: Send order confirmation email
        // TODO: Update inventory
        // TODO: Trigger fraud check
    }

    private void handleOrderPaid(OrderEvent event) {
        log.info("Processing ORDER_PAID for order {}", event.getOrderId());
        // TODO: Update order status in DB
        // TODO: Notify warehouse for fulfillment
    }

    private void handleOrderShipped(OrderEvent event) {
        log.info("Processing ORDER_SHIPPED for order {}", event.getOrderId());
        // TODO: Send shipping notification email
        // TODO: Update tracking info
    }

    private void handleOrderDelivered(OrderEvent event) {
        log.info("Processing ORDER_DELIVERED for order {}", event.getOrderId());
        // TODO: Send delivery confirmation
        // TODO: Request product review
    }

    private void handleOrderCancelled(OrderEvent event) {
        log.info("Processing ORDER_CANCELLED for order {}", event.getOrderId());
        // TODO: Process refund
        // TODO: Restore inventory
        // TODO: Send cancellation email
    }
}
