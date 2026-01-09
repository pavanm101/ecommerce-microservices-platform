package com.pavan.ecommerce.payment;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
@Slf4j
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.currency}")
    private String currency;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public PaymentResponse createCheckoutSession(PaymentRequest request) {
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/cart");

        // Add line items
        for (PaymentRequest.PaymentItem item : request.getItems()) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency(currency)
                                            .setUnitAmount(item.getPrice().multiply(new BigDecimal(100)).longValue()) // Amount
                                                                                                                      // in
                                                                                                                      // cents
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getName())
                                                            .build())
                                            .build())
                            .build());
        }

        // Add metadata for order tracking
        if (request.getOrderId() != null) {
            paramsBuilder.putMetadata("order_id", request.getOrderId().toString());
        }

        try {
            Session session = Session.create(paramsBuilder.build());
            return PaymentResponse.builder()
                    .paymentUrl(session.getUrl())
                    .sessionId(session.getId())
                    .build();
        } catch (Exception e) {
            log.error("Error creating Stripe session", e);
            throw new RuntimeException("Failed to create payment session", e);
        }
    }
}
