package com.tecnoin.order_service.model.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO{

    @NotBlank(message = "Payment status is required")
    private String paymentStatus;
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    @NotBlank(message = "Payment amount is required")
    private String paymentAmount;
}
