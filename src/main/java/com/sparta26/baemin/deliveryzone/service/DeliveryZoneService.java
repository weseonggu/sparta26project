package com.sparta26.baemin.deliveryzone.service;

import com.sparta26.baemin.deliveryzone.entity.DeliveryZone;
import com.sparta26.baemin.deliveryzone.repository.DeliveryZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryZoneService {

    private final DeliveryZoneRepository deliveryZoneRepository;
}
