package com.sparta26.baemin.order.repository;

import com.sparta26.baemin.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> getOrdersByRole(
            String status, String search, String role, String email, Pageable pageable
    );
}
