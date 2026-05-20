package com.tecnoin.payment_service.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments",catalog = "payments_db")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paymentStatus;
    private String paymentMethod;
    private BigDecimal paymentAmount;

    @PrePersist
    private void prePersist() {
        this.paymentStatus = "DONE";
    }


}
