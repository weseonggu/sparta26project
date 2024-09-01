package com.sparta26.baemin.dto.deliveryzone;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ResponseSearchDeliveryDto {

    private UUID delivery_id;
    private String name;
    private boolean is_possible;
    private UUID store_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @QueryProjection
    public ResponseSearchDeliveryDto(UUID delivery_id, String name, boolean is_possible, UUID store_id, LocalDateTime created_at, LocalDateTime updated_at) {
        this.delivery_id = delivery_id;
        this.name = name;
        this.is_possible = is_possible;
        this.store_id = store_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
