package com.sparta26.baemin.store.client;

import com.sparta26.baemin.operatinghours.entity.OperatingHours;
import com.sparta26.baemin.operatinghours.repository.OperatingHoursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OperatingHoursImpl implements OperatingHoursClient {

    private final OperatingHoursRepository operatingHoursRepository;

    @Override
    public List<OperatingHours> findByStoreId(UUID storeId) {
        return operatingHoursRepository.findByStoreId(storeId);
    }


}
