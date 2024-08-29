package com.sparta26.baemin.store.entity;

import com.sparta26.baemin.category.entity.Category;
import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.deliveryzone.entity.DeliveryZone;
import com.sparta26.baemin.dto.store.RequestStoreDto;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.operatinghours.entity.OperatingHours;
import com.sparta26.baemin.order.entity.Order;
import com.sparta26.baemin.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_STORES")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","name","description","address","phoneNumber","isActive"})
public class Store extends AuditEntity {

    @Id
    @GeneratedValue @UuidGenerator
    @Column(name = "store_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

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

    @OneToMany(mappedBy = "store")
    private List<OperatingHours> operatingHours = new ArrayList<>();

    public Store(String name, String description, String address, String phoneNumber, Member member, String email) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.phoneNumber = phoneNumber;
        if (member != null) {
            this.member = member;
            super.addCreatedBy(email);
        }
    }

    public Store(UUID id, String name, String description, String address, String phoneNumber, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
    }

    public Store(UUID id) {
        this.id = id;
    }

    /**
     * 생성 메서드
     */
    public static Store createStore(String name, String description, String address, String phoneNumber, Member member, String email) {
        return new Store(name, description, address, phoneNumber, member, email);
    }

    // 활성화 상태 변경
    public void changeActive() {
        if (this.isActive) {
            this.isActive = false;
        }else {
            this.isActive = true;
        }
    }

    public Store update(RequestStoreDto requestStoreDto, String email) {
        if (requestStoreDto.getName() != null) {
            this.name = requestStoreDto.getName();
        }
        if (requestStoreDto.getDescription() != null) {
            this.description = requestStoreDto.getDescription();
        }
        if (requestStoreDto.getAddress() != null) {
            this.address = requestStoreDto.getAddress();
        }
        if (requestStoreDto.getPhone_number() != null) {
            this.phoneNumber = requestStoreDto.getPhone_number();
        }
        super.addUpdatedBy(email);
        return this;
    }
}
