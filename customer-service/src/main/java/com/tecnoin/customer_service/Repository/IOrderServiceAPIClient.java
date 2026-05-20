package com.tecnoin.customer_service.Repository;

import com.tecnoin.customer_service.model.dto.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Hidden
@FeignClient(name = "order-service", path = "/api/v1/order")
public interface IOrderServiceAPIClient {
    @GetMapping("orders/orders-customer")
    Page<OrderResponseDTO> getOrdersById(@RequestParam("customerId") Long customerId);
}
