package com.tecnoin.payment_service.controller;

import com.tecnoin.payment_service.model.dto.PaymentRequestDTO;
import com.tecnoin.payment_service.service.IPaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final IPaymentService paymentService;
    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }
    @PostMapping
    public ResponseEntity<?> savePayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        Map<String, Object> response = new HashMap<>();
        response.put("message","Payment saved successfully");
        response.put("data", paymentService.savePayment(paymentRequestDTO));
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
}
