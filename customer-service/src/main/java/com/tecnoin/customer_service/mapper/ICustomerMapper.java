package com.tecnoin.customer_service.mapper;

import com.tecnoin.customer_service.model.dto.CustomerRequestDTO;
import com.tecnoin.customer_service.model.dto.CustomerResponseDTO;
import com.tecnoin.customer_service.model.entities.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICustomerMapper {

    Customer customerRequestDTOtoCustomer(CustomerRequestDTO customerRequestDTO);
    List<CustomerResponseDTO> customerListToCustomerResponseDTOList(List<Customer> customerList);
    CustomerResponseDTO customerToCustomerResponseDTO(Customer customer);
}
