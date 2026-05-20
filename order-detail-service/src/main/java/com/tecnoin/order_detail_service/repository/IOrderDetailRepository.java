package com.tecnoin.order_detail_service.repository;

import com.tecnoin.order_detail_service.model.entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Page<OrderDetail> findAllByOrderId(Long orderId, Pageable pageable);
}
