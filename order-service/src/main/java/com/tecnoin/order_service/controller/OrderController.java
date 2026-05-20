package com.tecnoin.order_service.controller;

import com.tecnoin.order_service.model.dto.OrderRequestDTO;
import com.tecnoin.order_service.service.IOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final IOrderService orderService;
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(Pageable pageable){
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @GetMapping("/orders-customer")
    public ResponseEntity<?> getOrdersUser(Pageable pageable,@RequestParam Long customerId){
        return ResponseEntity.ok(orderService.getOrdersUser(pageable, customerId));
    }
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO){
        Map<String, Object> response = new HashMap<>();
        response.put("message", orderService.createOrder(orderRequestDTO));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/update-status")
    public ResponseEntity<?> updateStatusOrder(@PathVariable Long id, @RequestParam String status){
        return new ResponseEntity<>(orderService.updateStatusOrder(id, status), HttpStatus.OK);
    }
}
