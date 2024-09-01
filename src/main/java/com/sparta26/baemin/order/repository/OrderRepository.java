package com.sparta26.baemin.order.repository;

import com.sparta26.baemin.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {
    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.member.id = :memberId AND o.isPublic = true")
    Optional<Order> findByIdAndMember_IdAndIsPublic(UUID id, Long memberId);

    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.store.id = :storeId AND o.isPublic = true")
    Optional<Order> findByIdAndStore_IdAndIsPublic(UUID id, UUID storeId);


}
