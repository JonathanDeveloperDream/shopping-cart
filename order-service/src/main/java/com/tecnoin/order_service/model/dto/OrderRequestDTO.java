package com.tecnoin.order_service.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {


    @NotBlank(message = "shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "country is required")
    private String country;

    private String notes;

    @NotNull(message = "quantity is required")
    private OrderDetailRequestDTO orderDetailRequestDTO;

    @NotNull(message = "payment is required")
    private PaymentRequestDTO paymentRequestDTO;

    @NotNull(message = "customer id is required")
    private Long customerId;

    @NotNull(message = "product id is required")
    private Long ProductId;

}
