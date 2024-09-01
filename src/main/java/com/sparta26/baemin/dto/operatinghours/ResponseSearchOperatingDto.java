package com.sparta26.baemin.dto.operatinghours;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class ResponseSearchOperatingDto {

    private UUID operating_hours_id;
    private String opening_time;
    private String closing_time;
    private String open_days;
    private String last_order;
    private UUID store_id;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @QueryProjection
    public ResponseSearchOperatingDto(UUID operating_hours_id, String opening_time, String closing_time, String open_days, String last_order, UUID store_id, LocalDateTime created_at, LocalDateTime updated_at) {
        this.operating_hours_id = operating_hours_id;
        this.opening_time = opening_time;
        this.closing_time = closing_time;
        this.open_days = open_days;
        this.last_order = last_order;
        this.store_id = store_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
