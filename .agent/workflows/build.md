---
description: How to build and test the project
---

# Build and Test

// turbo-all

## Build

1. Clean and compile:
```bash
mvn clean compile
```

2. Run tests:
```bash
mvn test
```

3. Package as JAR:
```bash
mvn package -DskipTests
```

## Docker Build

1. Build Docker image:
```bash
docker build -t spring-ecommerce:latest .
```
