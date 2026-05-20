package com.tecnoin.order_service.service.impl;

import com.tecnoin.order_service.mapper.IOrderMapper;
import com.tecnoin.order_service.model.dto.OrderRequestDTO;
import com.tecnoin.order_service.model.dto.OrderResponseDTO;
import com.tecnoin.order_service.model.entities.Order;
import com.tecnoin.order_service.repository.ICustomerServiceAPIClient;
import com.tecnoin.order_service.repository.IOrderDetailServiceAPIClient;
import com.tecnoin.order_service.repository.IOrderRepository;
import com.tecnoin.order_service.repository.IPaymentServiceAPIClient;
import com.tecnoin.order_service.service.IOrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {

    // Injecting the dependencies
    private final IOrderRepository orderRepository;
    private final IOrderMapper orderMapper;
    private final IPaymentServiceAPIClient paymentServiceAPIClient;
    private final IOrderDetailServiceAPIClient orderDetailServiceAPIClient;
    private final ICustomerServiceAPIClient customerServiceAPIClient;

    public OrderServiceImpl(IOrderRepository orderRepository, IOrderMapper orderMapper,
                            ICustomerServiceAPIClient customerServiceAPIClient, IOrderDetailServiceAPIClient orderDetailServiceAPIClient,
                            IPaymentServiceAPIClient paymentServiceAPIClient
    ) {
        this.orderRepository = orderRepository;
        this.orderDetailServiceAPIClient = orderDetailServiceAPIClient;
        this.customerServiceAPIClient = customerServiceAPIClient;
        this.orderMapper = orderMapper;
        this.paymentServiceAPIClient = paymentServiceAPIClient;
    }

    // Implementing the methods
    //Method to get all orders
    @Override
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        //Getting all orders in Pageable format
        Page<Order> ordersPage = orderRepository.findAll(pageable);

        //Mapping the orders to OrderResponseDTO
        List<OrderResponseDTO> orderResponseDTOList =
                orderMapper.OrderToOrderResponseDTOList(ordersPage.getContent());
        //Returning the PageImpl with the mapped orders
        return new PageImpl<>(orderResponseDTOList, pageable, ordersPage.getTotalElements());
    }

    //Method to get orders by customerId
    @Override
    public Page<OrderResponseDTO> getOrdersUser(Pageable pageable, Long customerId) {
        //Getting all orders by customerId in Pageable format
        Page<Order> ordersPage = orderRepository.findAllByCustomerId(customerId, pageable);
        //Mapping the orders to OrderResponseDTO
        List<OrderResponseDTO> orderResponseDTOList =
                orderMapper.OrderToOrderResponseDTOList(ordersPage.getContent());
        //Returning the PageImpl with the mapped orders
        return new PageImpl<>(orderResponseDTOList, pageable, ordersPage.getTotalElements());
    }

    //Method to create an order
    @Override
    @Transactional
    @CircuitBreaker(name = "order-detail-service", fallbackMethod = "createOrderFallback")
    @Retry(name = "order-detail-service")
    public String createOrder(OrderRequestDTO orderRequestDTO) {
        //Validating the request
        if (orderRequestDTO.getProductId() == null || orderRequestDTO.getCustomerId() == null) {
            throw new IllegalArgumentException("Product or Customer not found");
        }

        //Creating the order
        Order order = orderMapper.OrderRequestDTOToOrder(orderRequestDTO);
        UUID uuid = UUID.randomUUID();
        String uuidComoTexto = uuid.toString();
        order.setOrderNumber(uuidComoTexto);

        Order savedOrder = orderRepository.save(order);

        //Creating the order detail
        orderRequestDTO.getOrderDetailRequestDTO().setOrderId(savedOrder.getId());
        orderRequestDTO.getOrderDetailRequestDTO().setProductId(orderRequestDTO.getProductId());
        orderDetailServiceAPIClient.createOrderDetail(orderRequestDTO.getOrderDetailRequestDTO());

        //Creating the payment
        Map<String, Object> paymentResponse = paymentServiceAPIClient.createPayment(orderRequestDTO.getPaymentRequestDTO());

        Map<String, Object> paymentData = (Map<String, Object>) paymentResponse.get("data");

        Long paymentId = Long.valueOf(paymentData.get("id").toString());

        savedOrder.setPaymentId(paymentId);
        orderRepository.save(savedOrder);

        //Returning the message
        return "Order created";
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        //Validating the id
        if (id == null) {
            throw new IllegalArgumentException("The id cannot be null");
        }

        //Getting the order by id
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id ));

        //Validating the customerId
        if (order.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer not found for order with id: " + id + "");
        }

        //Mapping the order to OrderResponseDTO
        OrderResponseDTO orderResponseDTO = orderMapper.OrderToOrderResponseDTO(order);
        //setting the customer
        orderResponseDTO.setCustomer(customerServiceAPIClient.getCustomerById(order.getCustomerId()));

        //Returning the OrderResponseDTO
        orderResponseDTO.setPayment(paymentServiceAPIClient.getPaymentById(order.getPaymentId()));
        return orderResponseDTO;
    }

    @Override
    @Transactional
    public String updateStatusOrder(Long id, String status) {
        if (id == null) {
            throw new IllegalArgumentException("The order id cannot be null");
        }

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("The order status cannot be null or empty");
        }

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        order.setDeliveredAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(status);

        orderRepository.save(order);

        return "Order status updated";
    }

    //Fallback method for createOrder
    public String createOrderFallback(OrderRequestDTO orderRequestDTO, Throwable throwable){
        return "Order not created, please try again later";
    }
}
