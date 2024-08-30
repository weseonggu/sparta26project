package com.sparta26.baemin.payment.repository;

import com.sparta26.baemin.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID>, PaymentRepositoryCustom {
    Optional<Payment> findByIdAndCreatedBy(UUID paymentId, String email);
}
