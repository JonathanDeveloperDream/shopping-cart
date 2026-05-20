package com.tecnoin.order_service.repository;

import com.tecnoin.order_service.model.dto.OrderDetailRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "order-detail-service", path = "/api/v1/order-detail")
public interface IOrderDetailServiceAPIClient {

    @PostMapping("/order-details")
    Map<String, Object> createOrderDetail(@RequestBody OrderDetailRequestDTO orderDetailRequestDTO);
}
