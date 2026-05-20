package com.tecnoin.payment_service.service;


import com.tecnoin.payment_service.model.dto.PaymentRequestDTO;
import com.tecnoin.payment_service.model.dto.PaymentResponseDTO;

public interface IPaymentService {
    PaymentResponseDTO savePayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO getPaymentById(Long id);
}
