package com.pavan.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka topic configuration.
 * Creates topics for order and product events.
 */
@Configuration
public class KafkaTopicConfig {

    public static final String ORDER_EVENTS_TOPIC = "order-events";
    public static final String PRODUCT_EVENTS_TOPIC = "product-events";
    public static final String ANALYTICS_EVENTS_TOPIC = "analytics-events";

    @Bean
    public NewTopic orderEventsTopic() {
        return TopicBuilder.name(ORDER_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productEventsTopic() {
        return TopicBuilder.name(PRODUCT_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic analyticsEventsTopic() {
        return TopicBuilder.name(ANALYTICS_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
