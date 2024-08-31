package com.sparta26.baemin.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestSearchMemberDto {
    private String email;
    private Boolean isPublic;
}
