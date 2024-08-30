package com.sparta26.baemin.payment.service;

import com.sparta26.baemin.dto.payment.RequestPaymentDto;
import com.sparta26.baemin.dto.payment.ResponsePaymentInfoDto;
import com.sparta26.baemin.exception.exceptionsdefined.BadRequestException;
import com.sparta26.baemin.exception.exceptionsdefined.NotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.UnauthorizedException;
import com.sparta26.baemin.member.entity.UserRole;
import com.sparta26.baemin.payment.entity.Payment;
import com.sparta26.baemin.payment.entity.PaymentResult;
import com.sparta26.baemin.payment.entity.PaymentStatus;
import com.sparta26.baemin.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    @Transactional
    public ResponsePaymentInfoDto pay(RequestPaymentDto request) {

        Payment payment = Payment.createPayment(
                PaymentStatus.PENDING,
                request.getCardNumber(),
                LocalDateTime.now(),
                request.getTotalPrice()
        );

        // 결제 생성 PENDING -> PG ->  결제 성공 COMPLETE

        PaymentResult pgResult = PaymentResult.SUCCESS;
        if (PaymentResult.FAIL.equals(pgResult)) {
            throw new BadRequestException("Pay failed.");
        }

        payment.updatePaymentStatus(PaymentStatus.COMPLETE);
        paymentRepository.save(payment);

        return entityToResponsePaymentInfoDto(payment);
    }

    @Transactional(readOnly = true)
    public ResponsePaymentInfoDto getPayment(String paymentId, String role, String email) {

        Optional<Payment> payment = switch (UserRole.fromString(role)) {
            case ROLE_CUSTOMER, ROLE_OWNER ->
                    paymentRepository.findByIdAndCreatedBy(UUID.fromString(paymentId), email);
            case ROLE_MANAGER, ROLE_MASTER ->
                    paymentRepository.findById(UUID.fromString(paymentId));
        };

        if (payment.isEmpty()) {
            throw new NotFoundException("Payment not found.");
        }

        return entityToResponsePaymentInfoDto(payment.get());
    }

    @Transactional(readOnly = true)
    public Page<ResponsePaymentInfoDto> getPayments(
            LocalDate startDate, LocalDate endDate, Pageable pageable,
            String email, String role
    ) {

        Page<Payment> payments =
                paymentRepository.getPaymentsByRole(
                        startDate, endDate, pageable, email, role
                );

        return payments.map(this::entityToResponsePaymentInfoDto);
    }

    @Transactional
    public ResponsePaymentInfoDto updatePayment(
            String paymentId, String status, String role
    ) {

        checkRole(role);

        Payment payment =
                paymentRepository.findById(UUID.fromString(paymentId))
                        .orElseThrow(() -> new NotFoundException("Payment not found."));

        payment.updatePaymentStatus(PaymentStatus.fromString(status));

        return entityToResponsePaymentInfoDto(payment);
    }

    @Transactional
    public Boolean deletePayment(String paymentId, String email, String role) {

        checkRole(role);

        Payment payment =
                paymentRepository.findById(UUID.fromString(paymentId))
                        .orElseThrow(() -> new NotFoundException("Payment not found."));

        payment.delete(email);

        return true;
    }

    private static void checkRole(String role) {
        if (!UserRole.ROLE_MASTER.equals(UserRole.fromString(role))) {
            throw new UnauthorizedException("Required permissions to access this resource");
        }
    }

    private ResponsePaymentInfoDto entityToResponsePaymentInfoDto(Payment payment) {
        return ResponsePaymentInfoDto.createPaymentInfo(
                payment.getId().toString(),
                payment.getStatus().name(),
                payment.getCardNumber(),
                payment.getPayDate(),
                payment.getTotalPrice()
        );
    }
}
