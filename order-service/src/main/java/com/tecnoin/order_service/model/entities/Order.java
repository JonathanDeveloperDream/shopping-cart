package com.tecnoin.order_service.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders",catalog = "orderdb")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número único de orden
    @Column(unique = true, nullable = false)
    private String orderNumber;

    private String status;

    // Dirección de envío
    private String shippingAddress;

    private String city;

    private String country;

    // Notas adicionales
    @Column(length = 1000)
    private String notes;

    // Fechas
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deliveredAt;

    // Cliente que realizó la orden
    private Long customerId;

    private Long paymentId;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }



}
