package com.sparta26.baemin.dto.deliveryzone;

import com.sparta26.baemin.deliveryzone.entity.DeliveryZone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDeliveryDto {

    private UUID delivery_zone_id;
    private String name;
    private boolean is_possible;
    private UUID store_id;

    /**
     * 배달 지역 생성 | 가게 주인
     * @param savedDelivery
     * @return
     */
    public static ResponseDeliveryDto toDto(DeliveryZone savedDelivery) {
        return new ResponseDeliveryDto(savedDelivery.getId(),
                savedDelivery.getName(),
                savedDelivery.isPossible(),
                savedDelivery.getStore().getId());
    }
}
