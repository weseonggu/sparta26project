package com.sparta26.baemin.address.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_ADDRESS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends AuditEntity {

    @Id
    @UuidGenerator
    @Column(name = "addrerss_id")
    private UUID id;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "road_address", nullable = false)
    private String roadAddress;

    @Column(name = "road_address_english", nullable = false)
    private String roadAddressEnglish;

    public Address(String zipCode, String roadAddress, String roadAddressEnglish, String username) {
        this.zipCode = zipCode;
        this.roadAddress = roadAddress;
        this.roadAddressEnglish = roadAddressEnglish;
        super.addCreatedBy(username);
    }
}
