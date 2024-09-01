package com.sparta26.baemin.operatinghours.repository;

import com.sparta26.baemin.dto.operatinghours.RequestSearchOperatingDto;
import com.sparta26.baemin.dto.operatinghours.ResponseSearchOperatingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OperatingHoursRepositoryCustom {
    Page<ResponseSearchOperatingDto> findAllOperating(Pageable pageable, RequestSearchOperatingDto condition);
}
