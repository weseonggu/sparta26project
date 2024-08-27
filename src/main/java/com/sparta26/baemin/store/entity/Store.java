package com.sparta26.baemin.store.entity;

import com.sparta26.baemin.category.entity.Category;
import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.deliveryzone.entity.DeliveryZone;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.order.entity.Order;
import com.sparta26.baemin.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_STORES")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends AuditEntity {

    @Id
    @GeneratedValue @UuidGenerator
    @Column(name = "store_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "opening_time")
    private String openingTime;

    @Column(name = "closing_time")
    private String closingTime;

    @Column(name = "open_days")
    private String openDays;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "is_active")
    private boolean isActive = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "store")
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<DeliveryZone> deliveryZones = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Category> categories = new ArrayList<>();

    public Store(String name, String description, String openingTime, String closingTime, String openDays, String address, String phoneNumber, Member member) {
        this.name = name;
        this.description = description;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.openDays = openDays;
        this.address = address;
        this.phoneNumber = phoneNumber;
        if (member != null) {
            this.member = member;
            super.addCreatedBy(member.getEmail());
        }
    }

    /**
     * 생성 메서드
     */
    public static Store createStore(String name, String description, String openingTime, String closingTime, String openDays, String address, String phoneNumber, Member member) {
        return new Store(name, description, openingTime, closingTime, openDays, address, phoneNumber, member);
    }


    // 활성화 상태 변경
    public void changeActive() {
        this.isActive = true;
    }
}
