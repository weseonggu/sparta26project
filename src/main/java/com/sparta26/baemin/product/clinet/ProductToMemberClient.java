package com.sparta26.baemin.product.clinet;

import com.sparta26.baemin.dto.member.ResponseMemberToProductDto;

public interface ProductToMemberClient {
    ResponseMemberToProductDto findById(Long memberId);
}
