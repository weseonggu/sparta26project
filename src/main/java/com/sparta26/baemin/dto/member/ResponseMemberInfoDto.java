package com.sparta26.baemin.dto.member;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.querydsl.core.annotations.QueryProjection;
import com.sparta26.baemin.dto.common.AuditDto;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonFilter("MemberInfoFilter")
public class ResponseMemberInfoDto extends AuditDto implements Serializable {
    private Long id;
    private String email;
    private String password;
    private String username;
    private String nickname;
    private UserRole role;

    public ResponseMemberInfoDto(Member member) {
        super(member);
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.role = member.getRole();
    }

    @QueryProjection
    public ResponseMemberInfoDto(Long id, String email, String password, String username, String nickname, UserRole role,
                                 LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy, LocalDateTime deletedAt, String deletedBy, boolean isPublic
    ) {
        super(createdAt, createdBy, updatedAt, updatedBy, deletedAt, deletedBy, isPublic);
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.role = role;
    }

    public static ResponseMemberInfoDto toDto(Member member) {
        return new ResponseMemberInfoDto(member);
    }

}
