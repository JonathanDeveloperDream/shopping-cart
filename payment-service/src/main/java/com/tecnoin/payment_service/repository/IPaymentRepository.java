package com.tecnoin.payment_service.repository;

import com.tecnoin.payment_service.model.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment,Long> {

}
