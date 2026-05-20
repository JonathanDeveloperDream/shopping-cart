package com.tecnoin.order_service.repository;

import com.tecnoin.order_service.model.dto.PaymentRequestDTO;
import com.tecnoin.order_service.model.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "payment-service", path = "/api/v1/payment")
public interface IPaymentServiceAPIClient {

    @PostMapping("/payments")
    Map<String, Object> createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO);

    @GetMapping("/payments/{id}")
    PaymentResponseDTO getPaymentById(@PathVariable Long id);
}
