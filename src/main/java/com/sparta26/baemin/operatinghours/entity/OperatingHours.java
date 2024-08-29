package com.sparta26.baemin.operatinghours.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.dto.operatinghours.RequestOperatingHoursDto;
import com.sparta26.baemin.dto.operatinghours.ResponseOperatingDto;
import com.sparta26.baemin.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_OPERATING_HOURS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OperatingHours extends AuditEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "operating_hours_id")
    private UUID id;

    @Column(name = "opening_time")
    private String openingTime;

    @Column(name = "closing_time")
    private String closingTime;

    @Column(name = "open_days")
    private String openDays;

    @Column(name = "last_order")
    private String lastOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    public Store store;

    public OperatingHours(String openingTime, String closingTime, String openDays, String lastOrder, String email) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.openDays = openDays;
        this.lastOrder = lastOrder;
        super.addCreatedBy(email);
    }

    /**
     * 운영시간 생성 시 사용!!
     */

    public OperatingHours(String openingTime, String closingTime, String openDays, String lastOrder, Store store, String email) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.openDays = openDays;
        this.lastOrder = lastOrder;
        if(store != null) {
            addStore(store);
        }
        super.addCreatedBy(email);
    }

    public OperatingHours(UUID id, String openingTime, String closingTime, String openDays, String lastOrder) {
        this.id = id;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.openDays = openDays;
        this.lastOrder = lastOrder;
    }

    public static OperatingHours toEntity(ResponseOperatingDto response) {
        return new OperatingHours(UUID.fromString(response.getOperating_hours_id()),
                response.getOpening_time(),
                response.getClosing_time(),
                response.getOpen_days(),
                response.getLast_order());
    }

    public void addStore(Store store) {
        this.store = store;
        store.getOperatingHours().add(this);
    }

    /**
     * 업데이트 메서드
     * @param requestOperatingHoursDto
     */
    public OperatingHours update(RequestOperatingHoursDto requestOperatingHoursDto, String email) {
        if (requestOperatingHoursDto.getOpening_time() != null) {
            this.openingTime = requestOperatingHoursDto.getOpening_time();
        }
        if (requestOperatingHoursDto.getClosing_time() != null) {
            this.closingTime = requestOperatingHoursDto.getClosing_time();
        }
        if (requestOperatingHoursDto.getOpen_days() != null) {
            this.openDays = requestOperatingHoursDto.getOpen_days();
        }
        if (requestOperatingHoursDto.getLast_order() != null) {
            this.lastOrder = requestOperatingHoursDto.getLast_order();
        }
        super.addUpdatedBy(email);
        return this;
    }
}
