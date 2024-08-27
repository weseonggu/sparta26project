package com.sparta26.baemin.store.repository;

import com.sparta26.baemin.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    @Query("select s from Store s where (s.address = :address or s.phoneNumber = :phoneNumber) and s.isPublic = true and s.member.id <> :memberId")
    Optional<Store> findByAddressOrPhoneNumberAndMemberIdNot(@Param("address") String address,
                                                             @Param("phoneNumber") String phoneNumber,
                                                             @Param("memberId") Long memberId);

    Optional<Store> findByIdAndMemberId(@Param("id") UUID id, @Param("member_id") Long member_id);

    @Query("select s from Store s left join fetch s.operatingHours where s.id = :id and s.isPublic = true")
    Page<Store> findByIdWithOperatingHours(@Param("id") UUID storeId, Pageable pageable);

    @Query("select s from Store s left join fetch s.operatingHours where s.isPublic = true")
    Page<Store> findPagingAll(Pageable pageable);

    @Query("select s from Store s where s.member.id = :memberId and s.isPublic = true")
    Optional<Store> findByMemberId(@Param("memberId") Long memberId);
}
