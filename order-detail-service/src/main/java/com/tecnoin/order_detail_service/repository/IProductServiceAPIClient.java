package com.tecnoin.order_detail_service.repository;

import com.tecnoin.order_detail_service.model.dto.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Hidden
@FeignClient(name = "fakestoreapi", url = "https://fakestoreapi.com")
public interface IProductServiceAPIClient {

    @GetMapping("/products")
    List<ProductResponseDTO> getAllProducts();

    @GetMapping("/products/{id}")
    ProductResponseDTO getProductById(@PathVariable Long id);
}
