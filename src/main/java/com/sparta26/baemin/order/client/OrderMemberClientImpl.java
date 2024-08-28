package com.sparta26.baemin.order.client;

import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMemberClientImpl implements OrderMemberClient {

    Member customer;

    private final MemberService memberService;
    @Override
    public Member getMemberById(Long memberId) {
        //TODO MemberService 에서 memberId 로 사용자 정보 가져오는 메서드
        return customer;
    }
}
