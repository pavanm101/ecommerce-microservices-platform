---
description: How to test Stripe payments
---

1. Ensure Backend is running (`mvn spring-boot:run`)
2. Ensure Frontend is running (`npm start` in frontend dir)

### 1. Configure Test Keys
Ensure `application.properties` has valid test keys:
```properties
stripe.api.key=sk_test_...
stripe.webhook.secret=whsec_...
```

### 2. Test Checkout API
**POST** `http://localhost:8080/api/payment/checkout`
```json
{
  "items": [
    {
      "name": "Test Product",
      "price": 100.00,
      "quantity": 1
    }
  ],
  "orderId": 123
}
```
Response should contain `paymentUrl`.

### 3. Test Webhook (Local)
Use Stripe CLI to forward events:
```bash
stripe listen --forward-to localhost:8080/api/webhook/stripe
```
Trigger event:
```bash
stripe trigger checkout.session.completed
```
