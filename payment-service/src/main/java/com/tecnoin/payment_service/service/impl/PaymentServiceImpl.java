package com.tecnoin.payment_service.service.impl;

import com.tecnoin.payment_service.mapper.IPaymentMapper;
import com.tecnoin.payment_service.model.dto.PaymentRequestDTO;
import com.tecnoin.payment_service.model.dto.PaymentResponseDTO;
import com.tecnoin.payment_service.repository.IPaymentRepository;
import com.tecnoin.payment_service.service.IPaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements IPaymentService {

    private final IPaymentRepository paymentRepository;
    private final IPaymentMapper paymentMapper;
    public PaymentServiceImpl(IPaymentRepository paymentRepository, IPaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        return paymentMapper.paymentToPaymentResponseDTO(paymentRepository.findById(id).get());
    }

    @Override
    public PaymentResponseDTO savePayment(PaymentRequestDTO paymentRequestDTO) {
       return paymentMapper.paymentToPaymentResponseDTO(paymentRepository.save(paymentMapper.paymentRequestDTOToPayment(paymentRequestDTO)));

    }
}
