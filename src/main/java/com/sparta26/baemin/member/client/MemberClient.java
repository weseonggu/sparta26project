package com.sparta26.baemin.member.client;

import com.sparta26.baemin.member.repository.MemberRepository;
import com.sparta26.baemin.order.controller.OrderController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberClient {
    private final OrderController orderController;

}
