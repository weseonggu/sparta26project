package com.sparta26.baemin.store.repository;

import com.sparta26.baemin.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    @Query("select s from Store s where (s.address = :address or s.phoneNumber = :phoneNumber) and s.isPublic = true and s.member.id <> :memberId")
    Optional<Store> findByAddressOrPhoneNumberAndMemberIdNot(@Param("address") String address,
                                                             @Param("phoneNumber") String phoneNumber,
                                                             @Param("memberId") Long memberId);

    @Query("select s from Store s  where s.id = :id and s.member.id = :member_id and s.isPublic = true")
    Optional<Store> findByIdAndMemberId(@Param("id") UUID id, @Param("member_id") Long member_id);

    @Query("select s from Store s left join fetch s.operatingHours o where s.id = :id and s.isPublic = true and o.isPublic = true")
    Page<Store> findByIdWithOperatingHours(@Param("id") UUID storeId, Pageable pageable);

    @Query("select s from Store s left join fetch s.operatingHours o where s.isPublic = true and o.isPublic = true")
    Page<Store> findPagingAll(Pageable pageable);

    @Query("select s from Store s where s.member.id = :memberId and s.isPublic = true")
    Optional<Store> findByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("update Store s set s.isPublic = false, s.deletedBy = :email, s.deletedAt = local datetime where s.id = :id and s.member.id = :memberId")
    void deleteByIdAndMemberId(@Param("id") UUID uuid,@Param("memberId") Long memberId, @Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Query("update Store s set s.isPublic = false, s.deletedBy = :email, s.deletedAt = local datetime where s.id = :id")
    void deleteById(@Param("id") UUID uuid,@Param("email") String email);


}
