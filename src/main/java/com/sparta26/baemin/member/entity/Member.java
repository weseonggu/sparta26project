package com.sparta26.baemin.member.entity;

import com.sparta26.baemin.address.entity.Address;
import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "p_MEMBERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole role; // ROLE_CUSTOMER, ROLE_OWNER, ROLE_MASTER, ROLE_MANAGER;

    @OneToMany @JoinColumn(name = "member_id")
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    @Builder
    public Member(String email, String password, String username, String nickname, UserRole role, Address... addresses) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.role = role;
        if (addresses != null) {
            addAddress(addresses);
        }
        super.addCreatedBy(email);
    }

    /**
     * 생성 메서드
     */
    public static Member createMember(String email, String password, String username, String nickname, UserRole role, Address... addresses) {
        return new Member(email, password, username, nickname, role, addresses);
    }

    public void addAddress(Address... address) {
        addresses.addAll(Arrays.asList(address));
    }


}
