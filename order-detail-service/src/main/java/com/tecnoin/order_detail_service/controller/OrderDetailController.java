package com.tecnoin.order_detail_service.controller;

import com.tecnoin.order_detail_service.model.dto.OrderDetailRequestDTO;
import com.tecnoin.order_detail_service.service.IOrderDetailService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order-details")
public class OrderDetailController {

    private final IOrderDetailService orderDetailService;
    public OrderDetailController(IOrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/products")
    public Object getAllProducts(Pageable pageable){
        return orderDetailService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailRequestDTO orderDetailRequestDTO){
        Map<String,Object> response = new HashMap<>();
        response.put("message",orderDetailService.createOrder(orderDetailRequestDTO));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<?> getAllOrdersDetails(Pageable pageable){
        return new ResponseEntity<>(orderDetailService.getAllOrdersDetails(pageable), HttpStatus.OK);
    }

    @GetMapping("/order-id/{orderId}")
    public ResponseEntity<?> getOrdersDetailsByOrderId(@PathVariable Long orderId, Pageable pageable){
        return new ResponseEntity<>(orderDetailService.getOrdersDetailsByOrderId(orderId, pageable), HttpStatus.OK);
    }
}
