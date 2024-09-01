package com.sparta26.baemin.address.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Address(String zipCode, String roadAddress, String roadAddressEnglish, String username, Long member_id) {
        this.zipCode = zipCode;
        this.roadAddress = roadAddress;
        this.roadAddressEnglish = roadAddressEnglish;
        this.member = new Member(member_id);
        super.addCreatedBy(username);
    }
}
