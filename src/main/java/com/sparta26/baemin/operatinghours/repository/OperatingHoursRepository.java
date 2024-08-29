package com.sparta26.baemin.operatinghours.repository;

import com.sparta26.baemin.operatinghours.entity.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperatingHoursRepository extends JpaRepository<OperatingHours, UUID> {

    @Query("select o from OperatingHours o where o.store.id = :id and o.openDays = :openDays and o.isPublic = true")
    OperatingHours findByStoreIdAndOpenDays(@Param("id") UUID id,@Param("openDays") String openDays);

    @Query("select o from OperatingHours o where o.id = :id and o.createdBy = :createdBy and o.isPublic = true")
    Optional<OperatingHours> findByIdAndCreatedBy(@Param("id") UUID id,@Param("createdBy") String createdBy);

    @Query("select o from OperatingHours o where o.isPublic = true and o.store.id = :id")
    List<OperatingHours> findByStoreId(@Param("id") UUID store_id);

    @Query("select o from OperatingHours o where o.isPublic = true and o.id = :id")
    Optional<OperatingHours> findIsPublicById(@Param("id") UUID id);
}
