package com.pavan.ecommerce.controllers;

import com.pavan.ecommerce.events.OrderEventPublisher;
import com.pavan.ecommerce.models.Order;
import com.pavan.ecommerce.services.EcommerceService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final EcommerceService ecommerceService;
    private final OrderEventPublisher orderEventPublisher;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        System.out.println("Webhook received: " + payload);

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

                if (session != null) {
                    handleCheckoutCompleted(session);
                }
            }

            return ResponseEntity.ok("Received");
        } catch (SignatureVerificationException e) {
            log.error("Invalid Stripe signature", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            log.error("Webhook processing error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    private void handleCheckoutCompleted(Session session) {
        String orderIdStr = session.getMetadata().get("order_id");
        if (orderIdStr != null) {
            Long orderId = Long.parseLong(orderIdStr);
            log.info("Payment successful for Order ID: {}", orderId);

            // Update local order status
            try {
                // Assuming services exist to get and update order
                // Order order = ecommerceService.getOrder(orderId);
                // order.setStatus("PAID");
                // ecommerceService.saveOrder(order);
                // orderEventPublisher.publishOrderPaid(order);
            } catch (Exception e) {
                log.error("Failed to update order status", e);
            }
        }
    }
}
