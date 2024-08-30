package com.sparta26.baemin.dto.member;

import com.sparta26.baemin.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberToProductDto {

    private Long member_id;
    private String email;
    private String username;
    private String nickname;
    private String role;

    public static ResponseMemberToProductDto toDto(Member member) {
        return new ResponseMemberToProductDto(member.getId(),
                member.getEmail(),
                member.getUsername(),
                member.getNickname(),
                member.getRole().getValue());

    }
}
