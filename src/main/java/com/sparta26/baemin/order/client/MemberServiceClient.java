package com.sparta26.baemin.order.client;

import com.sparta26.baemin.member.entity.Member;

public interface MemberServiceClient {
    Member getMemberById(Long memberId);
}
