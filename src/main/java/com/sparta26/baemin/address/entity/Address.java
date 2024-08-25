package com.sparta26.baemin.address.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_ADDRESS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends AuditEntity {

    @Id
    @Column(name = "zip_code")
    private Long zipCode;

    @Column(name = "road_address", nullable = false)
    private String roadAddress;

    @Column(name = "road_address_english", nullable = false)
    private String roadAddressEnglish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Address(Long zipCode, String roadAddress, String roadAddressEnglish, Member member) {
        this.zipCode = zipCode;
        this.roadAddress = roadAddress;
        this.roadAddressEnglish = roadAddressEnglish;
        if (member != null) {
            addMember(member);
        }
    }

    //== 생성 메서드 ==//
    public static Address createAddress(Long zipCode, String roadAddress, String roadAddressEnglish, Member member) {
        return new Address(zipCode, roadAddress, roadAddressEnglish, member);
    }

    public void addMember(Member member) {
        this.member = member;
        member.getAddresses().add(this);
    }

}
