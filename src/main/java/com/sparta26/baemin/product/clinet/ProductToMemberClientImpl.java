package com.sparta26.baemin.product.clinet;

import com.sparta26.baemin.dto.member.ResponseMemberToProductDto;
import com.sparta26.baemin.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductToMemberClientImpl implements ProductToMemberClient {

    private final MemberService memberService;

    @Override
    public ResponseMemberToProductDto findById(Long memberId) {
        return memberService.findByIdfromProduct(memberId);
    }
}
