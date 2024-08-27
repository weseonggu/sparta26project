package com.sparta26.baemin.store.client;

import com.sparta26.baemin.operatinghours.entity.OperatingHours;

import java.util.List;
import java.util.UUID;

public interface OperatingHoursClient {
    List<OperatingHours> findByStoreId(UUID storeId);
}
