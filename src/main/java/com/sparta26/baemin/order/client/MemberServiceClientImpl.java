package com.sparta26.baemin.order.client;

import com.sparta26.baemin.dto.member.ResponseMemberInfoDto;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.service.MemberCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceClientImpl implements MemberServiceClient {

    private final MemberCacheService memberCacheService;
    @Override
    public Member getMemberByEmail(String email) {

        ResponseMemberInfoDto memberInfo = memberCacheService.getMemberInfo(email);

        return convertToMember(memberInfo);
    }

    private Member convertToMember(ResponseMemberInfoDto response) {
        return Member.createMemberWithId(
                response.getId(),
                response.getEmail(),
                response.getPassword(),
                response.getUsername(),
                response.getNickname(),
                response.getRole()
        );
    }
}
