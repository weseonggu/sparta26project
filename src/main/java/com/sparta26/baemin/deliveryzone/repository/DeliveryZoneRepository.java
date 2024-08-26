package com.sparta26.baemin.deliveryzone.repository;

import com.sparta26.baemin.deliveryzone.entity.DeliveryZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, UUID> {
}
