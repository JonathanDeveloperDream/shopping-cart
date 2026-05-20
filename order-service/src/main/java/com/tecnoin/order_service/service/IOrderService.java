package com.tecnoin.order_service.service;

import com.tecnoin.order_service.model.dto.OrderRequestDTO;
import com.tecnoin.order_service.model.dto.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    Page<OrderResponseDTO> getAllOrders(Pageable pageable);
    Page<OrderResponseDTO> getOrdersUser(Pageable pageable, Long customerId);
    OrderResponseDTO getOrderById(Long id);
    String createOrder(OrderRequestDTO orderRequestDTO);
    String updateStatusOrder(Long id, String status);
    
}
