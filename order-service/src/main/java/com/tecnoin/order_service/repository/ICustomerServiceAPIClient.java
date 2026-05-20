package com.tecnoin.order_service.repository;

import com.tecnoin.order_service.model.dto.CustomerResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Hidden
@FeignClient(name = "customer-service", path = "/api/v1/customer")
public interface ICustomerServiceAPIClient {

    @GetMapping("/customers/{id}")
    CustomerResponseDTO getCustomerById(@PathVariable Long id);
}
