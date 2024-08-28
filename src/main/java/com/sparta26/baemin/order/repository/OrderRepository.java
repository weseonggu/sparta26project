package com.sparta26.baemin.order.repository;

import com.sparta26.baemin.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {
    Optional<Order> findByIdAndMember_Id(UUID id, Long memberId);
}
