package com.sparta26.baemin.order.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_ORDERS_PRODUCTS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends AuditEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "order_product_id")
    private UUID id;

    private Integer price;
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    //== 생성 메서드 ==//
    public static OrderProduct createOrderProduct(Integer price, Integer amount, Product product, String username) {
        return new OrderProduct(price, amount, product, username);
    }

    public OrderProduct(Integer price, Integer amount, Product product, String email) {
        this.price = price;
        this.amount = amount;
        this.product = product;
        super.addCreatedBy(email);

        product.removeStock(amount);
    }

    // 주문 취소 시 상품 수량 원복 메서드
    public void cancel() {
        getProduct().addStock(amount);
    }

    public void addOrder (Order order){
        this.order = order;
    }

    public int getTotalPrice () {
        return getPrice() * getAmount();
    }
}
