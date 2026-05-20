package com.tecnoin.order_service.mapper;

import com.tecnoin.order_service.model.dto.OrderRequestDTO;
import com.tecnoin.order_service.model.dto.OrderResponseDTO;
import com.tecnoin.order_service.model.entities.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderMapper {
    Order OrderRequestDTOToOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO OrderToOrderResponseDTO(Order order);
    List<OrderResponseDTO> OrderToOrderResponseDTOList(List<Order> order);

}
