package com.pavan.ecommerce.events;

import com.pavan.ecommerce.config.KafkaTopicConfig;
import com.pavan.ecommerce.search.ProductDocument;
import com.pavan.ecommerce.search.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Kafka consumer for product events.
 * Syncs product changes to Elasticsearch index.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventConsumer {

    private final ProductSearchRepository productSearchRepository;

    @KafkaListener(topics = KafkaTopicConfig.PRODUCT_EVENTS_TOPIC, groupId = "product-indexer-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeProductEvent(ProductEvent event) {
        log.info("Received product event: {} for product {}", event.getEventType(), event.getProductId());

        try {
            switch (event.getEventType()) {
                case ProductEvent.PRODUCT_CREATED:
                case ProductEvent.PRODUCT_UPDATED:
                    indexProduct(event);
                    break;
                case ProductEvent.PRODUCT_DELETED:
                    deleteProduct(event.getProductId());
                    break;
                default:
                    log.warn("Unknown product event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Error processing product event: {} for product {}",
                    event.getEventType(), event.getProductId(), e);
        }
    }

    private void indexProduct(ProductEvent event) {
        ProductDocument doc = ProductDocument.builder()
                .id(event.getProductId())
                .name(event.getProductName())
                .description(event.getDescription())
                .price(event.getPrice())
                .categoryId(event.getCategoryId())
                .categoryName(event.getCategoryName())
                .inStock(event.getInStock())
                .stockQuantity(event.getStockQuantity())
                .createdAt(LocalDateTime.now())
                .suggest(event.getProductName())
                .build();

        productSearchRepository.save(doc);
        log.info("Indexed product {} to Elasticsearch", event.getProductId());
    }

    private void deleteProduct(Long productId) {
        productSearchRepository.deleteById(productId);
        log.info("Deleted product {} from Elasticsearch", productId);
    }
}
