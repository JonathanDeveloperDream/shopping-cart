package com.tecnoin.order_service.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;

    private String orderNumber;

    private String status;

    private String shippingAddress;

    private String city;

    private String country;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deliveredAt;

    private CustomerResponseDTO customer;

    private PaymentResponseDTO payment;



  //  private Long productId;
}
