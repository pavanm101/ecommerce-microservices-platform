package com.pavan.ecommerce.controllers;

import com.pavan.ecommerce.payment.PaymentRequest;
import com.pavan.ecommerce.payment.PaymentResponse;
import com.pavan.ecommerce.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Stripe Payment APIs")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout")
    @Operation(summary = "Create Stripe Checkout session")
    public ResponseEntity<PaymentResponse> checkout(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createCheckoutSession(request);
        return ResponseEntity.ok(response);
    }
}
