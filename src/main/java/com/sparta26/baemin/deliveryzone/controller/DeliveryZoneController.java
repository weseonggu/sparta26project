package com.sparta26.baemin.deliveryzone.controller;

import com.sparta26.baemin.deliveryzone.entity.DeliveryZone;
import com.sparta26.baemin.deliveryzone.service.DeliveryZoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DeliveryZoneController {

    private final DeliveryZoneService deliveryZoneService;
}
