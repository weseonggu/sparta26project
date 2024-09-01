package com.sparta26.baemin.member.client;

import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.order.controller.OrderController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface MemberClient {
    public void deleteStoreinfo(String email, String role, Long memberId, Member member);

}
