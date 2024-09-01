package com.sparta26.baemin.dto.operatinghours;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSearchOperatingDto {

    private String opening_time;
    private String closing_time;
    private String open_days;
    private String last_order;
    private UUID store_id;
}
