package com.sparta26.baemin.payment.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_PAYMENTS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends AuditEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "payment_id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "pay_date")
    private LocalDateTime payDate;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @OneToOne(mappedBy = "payment")
    private Order order;

    @Builder(access = AccessLevel.PROTECTED)
    private Payment(UUID id, PaymentStatus status, String cardNumber, LocalDateTime payDate, Integer totalPrice) {
        this.id = id;
        this.status = status;
        this.cardNumber = cardNumber;
        this.payDate = payDate;
        this.totalPrice = totalPrice;
    }

    public static Payment createPayment(PaymentStatus status, String cardNumber, LocalDateTime payDate, Integer totalPrice) {
        return Payment.builder()
                .status(status)
                .cardNumber(cardNumber)
                .payDate(payDate)
                .totalPrice(totalPrice)
                .build();
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    public void updatePaymentStatus(PaymentStatus status) {
        this.status = status;
    }
}
