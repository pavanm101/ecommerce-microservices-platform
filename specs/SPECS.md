# Spring E-Commerce Platform - Project Specs

## Overview
Enterprise-grade e-commerce REST API with Elasticsearch search, Kafka event streaming, and observability.

## Technology Stack
| Category | Technology |
|----------|------------|
| Core | Java 17, Spring Boot 3.2 |
| Database | MySQL 8, Redis 7 |
| Search | Elasticsearch 8.11 |
| Messaging | Apache Kafka |
| API Docs | OpenAPI 3.0 (Springdoc) |
| Container | Docker, Docker Compose |

## API Endpoints

### Core (Existing)
- `POST /login` - JWT authentication
- `GET/POST /product` - Product CRUD
- `GET/POST /group` - Product groups
- `GET/POST /order` - Order management
- `GET/POST /cart` - Shopping cart

### Search (New)
- `GET /api/search/products` - Full-text search with facets
- `GET /api/search/autocomplete` - Search suggestions

### Analytics (New)
- `GET /api/analytics/sales-trends` - Sales aggregations
- `GET /api/analytics/top-products` - Best performers
- `GET /api/analytics/dashboard` - Combined metrics

## Event Topics (Kafka)
- `order-events` - Order lifecycle events
- `product-events` - Product updates for ES sync
- `analytics-events` - Aggregated analytics

## Infrastructure
Docker Compose provides:
- MySQL (port 3306)
- Redis (port 6379)
- Elasticsearch (port 9200)
- Kibana (port 5601)
- Kafka (port 9092)
- Zookeeper (port 2181)
