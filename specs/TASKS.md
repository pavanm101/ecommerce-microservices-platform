# Project Tasks

## Phase 1: Setup & Migration ⏳
- [x] Clone base repository
- [x] Create docker-compose.yml
- [x] Upgrade pom.xml (Spring Boot 3.2, Java 17)
- [x] Configure Git (local, personal account)
- [ ] Migrate javax.* → jakarta.*
- [ ] Fix Spring Security 6.x config
- [ ] Update application.properties
- [ ] Verify build compiles

## Phase 2: Elasticsearch Search
- [ ] Create ProductDocument
- [ ] Implement ProductSearchService
- [ ] Create SearchController
- [ ] Build AnalyticsService
- [ ] Create AnalyticsController
- [ ] Index products on startup

## Phase 3: Kafka Events
- [ ] Configure Kafka beans
- [ ] Create OrderEvent model
- [ ] Implement OrderEventPublisher
- [ ] Create AnalyticsEventConsumer
- [ ] Add WebSocket notifications

## Phase 4: Microservices (Later)
- [ ] API Gateway
- [ ] Product Service
- [ ] Order Service
- [ ] Analytics Service

## Phase 5: DevOps
- [ ] Dockerfile
- [ ] Prometheus metrics
- [ ] Grafana dashboards
- [ ] GitHub Actions CI/CD

## Phase 6: Features
- [ ] Stripe payment sandbox
- [ ] Recommendations
- [ ] Inventory tracking
