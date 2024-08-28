package com.sparta26.baemin.order.client;

import com.sparta26.baemin.member.entity.Member;

public interface OrderMemberClient {
    Member getMemberById(Long memberId);
}
