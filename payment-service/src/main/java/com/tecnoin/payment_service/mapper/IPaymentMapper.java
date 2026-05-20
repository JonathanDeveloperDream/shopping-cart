package com.tecnoin.payment_service.mapper;

import com.tecnoin.payment_service.model.dto.PaymentRequestDTO;
import com.tecnoin.payment_service.model.dto.PaymentResponseDTO;
import com.tecnoin.payment_service.model.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPaymentMapper {
    PaymentResponseDTO paymentToPaymentResponseDTO(Payment payment);
    Payment paymentRequestDTOToPayment(PaymentRequestDTO paymentRequestDTO);
}
