package com.tecnoin.order_detail_service.service.Impl;

import com.tecnoin.order_detail_service.mapper.IOrderDetailMapper;
import com.tecnoin.order_detail_service.model.dto.OrderDetailRequestDTO;
import com.tecnoin.order_detail_service.model.dto.OrderDetailResponseDTO;
import com.tecnoin.order_detail_service.model.dto.ProductResponseDTO;
import com.tecnoin.order_detail_service.model.entities.OrderDetail;
import com.tecnoin.order_detail_service.repository.IOrderDetailRepository;
import com.tecnoin.order_detail_service.repository.IOrderServiceAPIClient;
import com.tecnoin.order_detail_service.repository.IPaymentServiceAPIClient;
import com.tecnoin.order_detail_service.repository.IProductServiceAPIClient;
import com.tecnoin.order_detail_service.service.IOrderDetailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements IOrderDetailService {

    // Injecting the dependencies
    private final IProductServiceAPIClient productServiceAPIClient;
    private final IOrderServiceAPIClient orderServiceAPIClient;
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderDetailMapper orderDetailMapper;
    private final IPaymentServiceAPIClient paymentServiceAPIClient;

    public OrderDetailServiceImpl(IProductServiceAPIClient productServiceAPIClient,
                                  IOrderDetailRepository orderDetailRepository, IOrderDetailMapper orderDetailMapper,
                                  IOrderServiceAPIClient orderServiceAPIClient, IPaymentServiceAPIClient paymentServiceAPIClient) {
        this.productServiceAPIClient = productServiceAPIClient;
        this.orderServiceAPIClient = orderServiceAPIClient;
        this.paymentServiceAPIClient = paymentServiceAPIClient;
        this.orderDetailMapper = orderDetailMapper;
        this.orderDetailRepository = orderDetailRepository;
    }

    // Implementing the methods
    //Methods for Get All Products
    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productServiceAPIClient.getAllProducts();
    }

    //Methods for Create OrderDetail
    @Override
    public String createOrder(OrderDetailRequestDTO orderDetailRequestDTO) {
        //covert the DTO to entity
        OrderDetail order = orderDetailMapper.OrderDetailRequestDTOtoOrderDetail(orderDetailRequestDTO);
        //save the entity
        orderDetailRepository.save(order);
        //return the message
        return "Order Detail created successfully";
    }

    //Methods for Get All OrdersDetails
    @Override
    public Page<OrderDetailResponseDTO> getAllOrdersDetails(Pageable pageable) {
        //get all the orders details in Pageable format
        Page<OrderDetail> orderDetailsPage = orderDetailRepository.findAll(pageable);
        //convert the Page<OrderDetail> to Page<OrderDetailResponseDTO>
        List<OrderDetailResponseDTO> orderDetailResponseDTOS = orderDetailMapper.OrderDetailtoOrderDetailResponseDTO(orderDetailsPage.getContent());
        return new PageImpl<>(orderDetailResponseDTOS, pageable, orderDetailsPage.getTotalElements());
    }

    //Methods for Get OrdersDetails By OrderId
    @Override
    public Page<OrderDetailResponseDTO> getOrdersDetailsByOrderId(Long orderId, Pageable pageable) {
        //get all the orders details in Pageable format
        Page<OrderDetail> orderDetailsPage = orderDetailRepository.findAllByOrderId(orderId, pageable);
        //convert the Page<OrderDetail> to Page<OrderDetailResponseDTO>
        List<OrderDetailResponseDTO> orderDetailResponseDTOS = orderDetailMapper.OrderDetailtoOrderDetailResponseDTO(orderDetailsPage.getContent());
        //set the order and product an calculate the subtotal
        orderDetailResponseDTOS.forEach(orderDetailResponseDTO -> {
            orderDetailResponseDTO.setOrder(orderServiceAPIClient.getOrderById(orderId));
            orderDetailResponseDTO.setProduct(
                    productServiceAPIClient.getProductById(orderDetailResponseDTO.getProductId())
            );
            orderDetailResponseDTO.setSubtotal(
                    orderDetailResponseDTO.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(orderDetailResponseDTO.getQuantity()))
            );

        });
        //return the Page<OrderDetailResponseDTO>
        return new PageImpl<>(orderDetailResponseDTOS, pageable, orderDetailsPage.getTotalElements());
    }
}
