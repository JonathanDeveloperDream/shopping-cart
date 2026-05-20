package com.tecnoin.order_detail_service.service;

import com.tecnoin.order_detail_service.model.dto.OrderDetailRequestDTO;
import com.tecnoin.order_detail_service.model.dto.OrderDetailResponseDTO;
import com.tecnoin.order_detail_service.model.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderDetailService {
    List<ProductResponseDTO> getAllProducts();
    String createOrder(OrderDetailRequestDTO orderDetailRequestDTO);
    Page<OrderDetailResponseDTO> getAllOrdersDetails(Pageable pageable);
    Page<OrderDetailResponseDTO> getOrdersDetailsByOrderId(Long orderId, Pageable pageable);
}
