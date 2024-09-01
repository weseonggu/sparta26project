package com.sparta26.baemin.deliveryzone.repository;

import com.sparta26.baemin.dto.deliveryzone.RequestSearchDeliveryDto;
import com.sparta26.baemin.dto.deliveryzone.ResponseSearchDeliveryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryZoneRepositoryCustom {
    Page<ResponseSearchDeliveryDto> findAllDelivery(RequestSearchDeliveryDto condition, Pageable pageable);
}
