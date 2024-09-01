package com.sparta26.baemin.order.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.member.entity.Member;
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
    private OrderType orderType;

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

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public static Order createOrder(String address, OrderType orderType, String orderRequest, String deliveryRequest, Member member, Store store, String email, OrderStatus status, OrderProduct... orderProducts) {
        return new Order(address, orderType, orderRequest, deliveryRequest, member, store, email, status, orderProducts);
    }

    public Order(String address, OrderType orderType, String orderRequest, String deliveryRequest, Member member, Store store, String email, OrderStatus status, OrderProduct... orderProducts) {
        this.address = address;
        this.orderType = orderType;
        this.orderRequest = orderRequest;
        this.deliveryRequest = deliveryRequest;
        this.status = status;
        if (member != null) {
            addMember(member);
        }
        if (store != null) {
            addStore(store);
        }
        for (OrderProduct orderProduct : orderProducts) {
            addOrderProduct(orderProduct);
        }
        this.totalPrice = getTotalPrice();
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
        this.status = OrderStatus.CONFIRM;
        payment.addOrder(this);
    }

    public int getTotalPrice() {
        return orderProducts.stream().mapToInt(OrderProduct::getTotalPrice).sum();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void updateOrder(String address, String orderRequest, String deliveryRequest) {
        updateCommonFields(address, orderRequest, deliveryRequest);
    }

    public void cancelOrder(OrderStatus status) {
        this.status = status;
    }

    private void updateCommonFields(String address, String orderRequest, String deliveryRequest) {
        this.address = address;
        this.orderRequest = orderRequest;
        this.deliveryRequest = deliveryRequest;
    }
}
