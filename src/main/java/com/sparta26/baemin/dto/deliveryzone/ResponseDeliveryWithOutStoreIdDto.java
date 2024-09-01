package com.sparta26.baemin.dto.deliveryzone;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta26.baemin.deliveryzone.entity.DeliveryZone;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
public class ResponseDeliveryWithOutStoreIdDto {

    private UUID delivery_zone_id;
    private String name;
    private boolean is_possible;

    @QueryProjection
    public ResponseDeliveryWithOutStoreIdDto(UUID delivery_zone_id, String name, boolean is_possible) {
        this.delivery_zone_id = delivery_zone_id;
        this.name = name;
        this.is_possible = is_possible;
    }

    public static List<ResponseDeliveryWithOutStoreIdDto> toDtoList(List<DeliveryZone> deliveryZones) {
        List<ResponseDeliveryWithOutStoreIdDto> dtos = new ArrayList<>();
        for (DeliveryZone deliveryZone : deliveryZones) {
            if (deliveryZone.isPossible()) {
                ResponseDeliveryWithOutStoreIdDto dto = new ResponseDeliveryWithOutStoreIdDto();
                dto.delivery_zone_id = deliveryZone.getId();
                dto.name = deliveryZone.getName();
                dto.is_possible = deliveryZone.isPossible();
                dtos.add(dto);
            }
        }
        return dtos;
    }
}
