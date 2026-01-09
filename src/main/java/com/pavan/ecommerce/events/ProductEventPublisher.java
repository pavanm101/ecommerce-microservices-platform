package com.pavan.ecommerce.events;

import com.pavan.ecommerce.config.KafkaTopicConfig;
import com.pavan.ecommerce.models.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka producer for product events.
 * Publishes product changes to sync with Elasticsearch.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {

    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    /**
     * Publish product created event
     */
    public void publishProductCreated(Product product) {
        ProductEvent event = buildProductEvent(product, ProductEvent.PRODUCT_CREATED);
        publishEvent(event);
    }

    /**
     * Publish product updated event
     */
    public void publishProductUpdated(Product product) {
        ProductEvent event = buildProductEvent(product, ProductEvent.PRODUCT_UPDATED);
        publishEvent(event);
    }

    /**
     * Publish product deleted event
     */
    public void publishProductDeleted(Long productId) {
        ProductEvent event = ProductEvent.builder()
                .productId(productId)
                .eventType(ProductEvent.PRODUCT_DELETED)
                .eventTime(LocalDateTime.now())
                .build();
        publishEvent(event);
    }

    /**
     * Build ProductEvent from Product entity
     */
    private ProductEvent buildProductEvent(Product product, String eventType) {
        return ProductEvent.builder()
                .productId(product.getId())
                .eventType(eventType)
                .eventTime(LocalDateTime.now())
                .productName(product.getName())
                .description(product.getDescription())
                .price(product.getPrice() != null ? new BigDecimal(product.getPrice()) : null)
                .categoryId(product.getGroup() != null ? product.getGroup().getId() : null)
                .categoryName(product.getGroup() != null ? product.getGroup().getGroupName() : null)
                .inStock(true)
                .stockQuantity(100) // Default stock
                .build();
    }

    /**
     * Publish event to Kafka
     */
    private void publishEvent(ProductEvent event) {
        String key = String.valueOf(event.getProductId());

        CompletableFuture<SendResult<String, ProductEvent>> future = kafkaTemplate
                .send(KafkaTopicConfig.PRODUCT_EVENTS_TOPIC, key, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Published product event: {} for product {} to partition {}",
                        event.getEventType(),
                        event.getProductId(),
                        result.getRecordMetadata().partition());
            } else {
                log.error("Failed to publish product event: {} for product {}",
                        event.getEventType(),
                        event.getProductId(),
                        ex);
            }
        });
    }
}
