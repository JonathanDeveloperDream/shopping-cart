package com.tecnoin.order_detail_service.mapper;

import com.tecnoin.order_detail_service.model.dto.OrderDetailRequestDTO;
import com.tecnoin.order_detail_service.model.dto.OrderDetailResponseDTO;
import com.tecnoin.order_detail_service.model.entities.OrderDetail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderDetailMapper {

    OrderDetail OrderDetailRequestDTOtoOrderDetail(OrderDetailRequestDTO orderDetailRequestDTO);
    List<OrderDetailResponseDTO> OrderDetailtoOrderDetailResponseDTO(List<OrderDetail> orderDetails);

}
