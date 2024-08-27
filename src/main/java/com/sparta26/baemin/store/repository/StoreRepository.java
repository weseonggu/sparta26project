package com.sparta26.baemin.store.repository;

import com.sparta26.baemin.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findByMemberId(Long memberId);
}
