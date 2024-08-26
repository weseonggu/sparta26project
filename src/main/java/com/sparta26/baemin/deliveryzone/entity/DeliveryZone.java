package com.sparta26.baemin.deliveryzone.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_DELIVERY_ZONE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryZone extends AuditEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "delivery_zone_id")
    private UUID id;

    private String name;
    private boolean isPossible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public DeliveryZone(String name, Store store, String username) {
        this.name = name;
        if (store != null) {
            addStore(store);
        }
        super.addCreatedBy(username);
    }

    /**
     * 생성 메서드
     */
    public static DeliveryZone createDeliveryZone(String name, Store store, String username) {
        return new DeliveryZone(name, store, username);
    }

    public void addStore(Store store) {
        this.store = store;
        store.getDeliveryZones().add(this);
    }

    /**
     * 배달 가능 상태 변경
     */
    public void changePossible() {
        isPossible = false;
    }


}
