package com.sparta26.baemin.order.entity;

import com.sparta26.baemin.common.AuditEntity;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.orderproduct.entity.OrderProduct;
import com.sparta26.baemin.payment.entity.Payment;
import com.sparta26.baemin.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_ORDERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AuditEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "order_id")
    private UUID id;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private OrderType orderType; // ONLINE, OFFLINE

    @Column(name = "order_request")
    private String orderRequest;

    @Column(name = "delivery_request")
    private String deliveryRequest;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    //== 생성 메서드 ==//
    public static Order createOrder(String address, OrderType orderType, String orderRequest, String deliveryRequest, Integer totalPrice, Member member, Store store, List<OrderProduct> orderProducts, Payment payment) {
        return new Order(address, orderType, orderRequest, deliveryRequest, totalPrice, member, store, orderProducts, payment);
    }

    public Order(String address, OrderType orderType, String orderRequest, String deliveryRequest, Integer totalPrice, Member member, Store store, List<OrderProduct> orderProducts, Payment payment) {
        this.address = address;
        this.orderType = orderType;
        this.orderRequest = orderRequest;
        this.deliveryRequest = deliveryRequest;
        this.totalPrice = totalPrice;
        if (member != null) {
            addMember(member);
        }
        if (store != null) {
            addStore(store);
        }
        for (OrderProduct orderProduct : orderProducts) {
            addOrderProduct(orderProduct);
        }
        if (payment != null) {
            addPayment(payment);
        }
    }

    public void addMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addStore(Store store) {
        this.store = store;
        store.getOrders().add(this);
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.addOrder(this);
    }

    public void addPayment(Payment payment) {
        this.payment = payment;
        payment.addOrder(this);
    }

    // 전체 주문 가격 조회
    public int getTotalPrice() {
        return orderProducts.stream().mapToInt(OrderProduct::getPrice).sum();
    }
}