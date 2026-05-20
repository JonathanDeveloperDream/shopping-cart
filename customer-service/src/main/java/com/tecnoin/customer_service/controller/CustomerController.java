package com.tecnoin.customer_service.controller;

import com.tecnoin.customer_service.model.dto.CustomerRequestDTO;
import com.tecnoin.customer_service.service.ICustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final ICustomerService customerService;
    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping
    public ResponseEntity<?> getCustomers(Pageable pageable) {
        return ResponseEntity.ok(customerService.getCustomers(pageable));
    }

    @PostMapping
    public ResponseEntity<?> saveCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        Map<String,Object> response = new HashMap<>();
        response.put("message", customerService.saveCustomer(customerRequestDTO));
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }
}
