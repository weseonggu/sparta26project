package com.sparta26.baemin.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenAndMemberInfoDto {
    private String token;
    private ResponseMemberInfoDto memberInfo;
}
