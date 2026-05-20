package com.tecnoin.order_detail_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseDTO {

    private Long id;

    private Integer quantity;

    private BigDecimal subtotal;

    private Long productId;


    private OrderResponseDTO order;
    
    private ProductResponseDTO product;
}
