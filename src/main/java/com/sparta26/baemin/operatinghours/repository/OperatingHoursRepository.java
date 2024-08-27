package com.sparta26.baemin.operatinghours.repository;

import com.sparta26.baemin.operatinghours.entity.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperatingHoursRepository extends JpaRepository<OperatingHours, UUID> {

    OperatingHours findByStoreIdAndOpenDays(UUID store_id, String openDays);
    Optional<OperatingHours> findByIdAndCreatedBy(UUID id, String createdBy);

    @Query("select o from OperatingHours o where o.isPublic = true")
    List<OperatingHours> findByStoreId(UUID store_id);
}
