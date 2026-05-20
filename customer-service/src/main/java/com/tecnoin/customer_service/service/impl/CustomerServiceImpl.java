package com.tecnoin.customer_service.service.impl;

import com.tecnoin.customer_service.Repository.ICustomerRepository;
import com.tecnoin.customer_service.Repository.IOrderServiceAPIClient;
import com.tecnoin.customer_service.Repository.IUserServiceAPIClient;
import com.tecnoin.customer_service.mapper.ICustomerMapper;
import com.tecnoin.customer_service.model.dto.CustomerRequestDTO;
import com.tecnoin.customer_service.model.dto.CustomerResponseDTO;
import com.tecnoin.customer_service.model.dto.OrderResponseDTO;
import com.tecnoin.customer_service.model.entities.Customer;
import com.tecnoin.customer_service.service.ICustomerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService {

    //Injecting the dependencies
    private final IOrderServiceAPIClient orderServiceAPIClient;
    private final IUserServiceAPIClient userServiceAPIClient;
    private final ICustomerRepository customerRepository;
    private final ICustomerMapper customerMapper;


    public CustomerServiceImpl(IOrderServiceAPIClient orderServiceAPIClient,
                               ICustomerRepository customerRepository, ICustomerMapper customerMapper,
                               IUserServiceAPIClient userServiceAPIClient
    ) {
        this.userServiceAPIClient = userServiceAPIClient;
        this.orderServiceAPIClient = orderServiceAPIClient;
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    //Implementing the methods
    @Override
    //implementing the circuit breaker and retry
    @CircuitBreaker(name = "order-service", fallbackMethod = "fallbackGetUser")
    @Retry(name = "order-service")
    public Page<CustomerResponseDTO> getCustomers(Pageable pageable) {
        //Getting the customers in pageable format
        Page<Customer> customers = customerRepository.findAll(pageable);
        //Mapping the customers to the CustomerResponseDTO
        List<CustomerResponseDTO> customerResponseDTOS = customerMapper.customerListToCustomerResponseDTOList(customers.getContent());
        //Getting the user information of each customer
        customerResponseDTOS.forEach(customerResponseDTO -> {
            customers.forEach(customer -> {
                customerResponseDTO.setUser(userServiceAPIClient.getUserById(customer.getUserId()));
            });
        });
        //Returning the page of customers
        return new PageImpl<>(customerResponseDTOS, pageable, customers.getTotalElements());
    }

    //Method to save a customer
    @Override
    public String saveCustomer(CustomerRequestDTO customerRequestDTO) {
        //Checking if the user is already a customer and if the user exists
        if (customerRepository.existsByUserId(customerRequestDTO.getUserId())) {
            throw new IllegalArgumentException("This user is already a customer");
        } else if (!userServiceAPIClient.isExisteUserById(customerRequestDTO.getUserId())) {
            throw new IllegalArgumentException("This userId does not exist");
        }
        //Saving the customer
        customerRepository.save(customerMapper.customerRequestDTOtoCustomer(customerRequestDTO));
        //Returning a message
        return " User " + customerRequestDTO.getUserId() + " is now a customer";
    }

    //Getting a customer by id
    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        //Getting the customer by id
        Customer customer = customerRepository.findById(id).orElseThrow();
        //Mapping the customer to the CustomerResponseDTO
        CustomerResponseDTO customerResponseDTO = customerMapper.customerToCustomerResponseDTO(customer);
        //Getting the user information of the customer
        customerResponseDTO.setUser(userServiceAPIClient.getUserById(customer.getUserId()));
        return customerResponseDTO;
    }


    public Page<CustomerResponseDTO> fallbackGetUser(
            Pageable pageable,
            Throwable throwable
    ) {

        CustomerResponseDTO order = new CustomerResponseDTO();

        return new PageImpl<>(List.of(order));
    }

}
