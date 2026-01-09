---
description: How to run the development environment
---

# Development Environment Setup

// turbo-all

## Prerequisites
- Java 17+
- Maven 3.8+
- Docker Desktop

## Steps

1. Start infrastructure services:
```bash
docker-compose up -d mysql redis elasticsearch kafka zookeeper
```

2. Wait for services to be healthy (about 30 seconds)

3. Run the Spring Boot application:
```bash
mvn spring-boot:run
```

4. Access the application:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Kibana: http://localhost:5601

## Stopping

```bash
docker-compose down
```

## Resetting Data

```bash
docker-compose down -v
```
