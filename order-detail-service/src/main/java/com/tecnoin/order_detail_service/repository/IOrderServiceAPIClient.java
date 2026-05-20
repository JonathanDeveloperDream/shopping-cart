package com.tecnoin.order_detail_service.repository;

import com.tecnoin.order_detail_service.model.dto.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Hidden
@FeignClient(name = "order-service", path = "/api/v1/order")
public interface IOrderServiceAPIClient {

    @GetMapping("/orders/orders-customer")
    Page<OrderResponseDTO> getOrdersById(@RequestParam("customerId") Long customerId);

    @GetMapping("/orders/{id}")
    OrderResponseDTO getOrderById(@PathVariable Long id);

}
