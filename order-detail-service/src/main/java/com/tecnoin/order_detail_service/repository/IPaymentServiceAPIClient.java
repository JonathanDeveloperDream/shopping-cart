package com.tecnoin.order_detail_service.repository;


import com.tecnoin.order_detail_service.model.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "payment-service", path = "/api/v1/payment")
public interface IPaymentServiceAPIClient {


    @GetMapping("/payments/{id}")
    PaymentResponseDTO getPaymentById(@PathVariable Long id);
}
