package com.sparta26.baemin.payment.repository;

import com.sparta26.baemin.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PaymentRepositoryCustom {
    Page<Payment> getPaymentsByRole(
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable,
            String email,
            String role
    );
}
