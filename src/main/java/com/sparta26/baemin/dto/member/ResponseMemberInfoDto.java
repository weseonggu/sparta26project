package com.sparta26.baemin.dto.member;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.sparta26.baemin.dto.common.AuditDto;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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

}
