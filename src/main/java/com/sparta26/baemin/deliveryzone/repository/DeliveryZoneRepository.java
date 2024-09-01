package com.sparta26.baemin.deliveryzone.repository;

import com.sparta26.baemin.deliveryzone.entity.DeliveryZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, UUID>, DeliveryZoneRepositoryCustom {

    @Query("select d from DeliveryZone d where d.id = :id and d.createdBy = :email and d.isPublic = true")
    Optional<DeliveryZone> findByIdAndCreatedBy(@Param("id") UUID deliveryId,@Param("email") String email);

    @Query("select d from DeliveryZone d where d.store.id = :storeId and d.name = :name and d.isPublic = true")
    Optional<DeliveryZone> findByStoreIdAndName(@Param("storeId") UUID storeId, @Param("name") String name);

    @Query("select d from DeliveryZone d where d.store.id = :id and d.name <> :name and d.isPublic = true")
    List<DeliveryZone> findDuplicatedByStoreIdAndName(@Param("id") UUID id, @Param("name")String name);
}
