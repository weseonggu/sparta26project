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
    public Member(Long id, String email, String password, String username, String nickname, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.role = role;

        super.addCreatedBy(email);
    }
    public static Member createMember(String email, String password, String username, String nickname, UserRole role) {
        return Member.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .role(role)
                .build();
    }

    public static Member createMemberWithId(Long id, String email, String password, String username, String nickname, UserRole role) {
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .role(role)
                .build();
    }
    public Member(Long memberId) {
        this.id=memberId;
    }

    public void addAddress(Address... address) {
        addresses.addAll(Arrays.asList(address));
    }

    public void updateBy(String username, String nickname, String updateBy) {
        super.updateBy(updateBy);
        this.nickname = nickname;
        this.username = username;
    }
}
