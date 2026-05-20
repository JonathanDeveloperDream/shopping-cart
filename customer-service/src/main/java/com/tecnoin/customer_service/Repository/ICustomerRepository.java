package com.tecnoin.customer_service.Repository;

import com.tecnoin.customer_service.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUserId(Long userId);
}
