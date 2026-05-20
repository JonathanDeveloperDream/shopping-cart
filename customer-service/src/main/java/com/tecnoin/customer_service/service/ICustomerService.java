package com.tecnoin.customer_service.service;

import com.tecnoin.customer_service.model.dto.CustomerRequestDTO;
import com.tecnoin.customer_service.model.dto.CustomerResponseDTO;
import com.tecnoin.customer_service.model.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {
    Page<CustomerResponseDTO> getCustomers(Pageable pageable);
    String saveCustomer(CustomerRequestDTO customerRequestDTO);
    CustomerResponseDTO getCustomerById(Long id);
}
