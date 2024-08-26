package com.sparta26.baemin.payment.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private PaymentStatus status; // COMPLETE, CANCEL

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "pay_date")
    private LocalDateTime payDate;

    @OneToOne(mappedBy = "payment")
    private Order order;

    public Payment(PaymentStatus status, String cardNumber, LocalDateTime payDate, String username) {
        this.status = status;
        this.cardNumber = cardNumber;
        this.payDate = payDate;
        super.addCreatedBy(username);
    }

    public void addOrder(Order order) {
        this.order = order;
    }
}
