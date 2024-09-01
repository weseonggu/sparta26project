package com.sparta26.baemin.dto.operatinghours;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta26.baemin.operatinghours.entity.OperatingHours;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ResponseOperatingDto {

    private UUID operating_hours_id;
    private String opening_time;
    private String closing_time;
    private String open_days;
    private String last_order;

    @QueryProjection
    public ResponseOperatingDto(UUID operating_hours_id, String opening_time, String closing_time, String open_days, String last_order) {
        this.operating_hours_id = operating_hours_id;
        this.opening_time = opening_time;
        this.closing_time = closing_time;
        this.open_days = open_days;
        this.last_order = last_order;
    }

    public static ResponseOperatingDto toDto(OperatingHours savedOperating) {
        return new ResponseOperatingDto(savedOperating.getId(),
                savedOperating.getOpeningTime(),
                savedOperating.getClosingTime(),
                savedOperating.getOpenDays(),
                savedOperating.getLastOrder());
    }

    public static List<ResponseOperatingDto> toDtoList(List<OperatingHours> operatingHours) {
        List<ResponseOperatingDto> dtos = new ArrayList<>();
        for (OperatingHours operatingHour : operatingHours) {
            if (operatingHour.isPublic()) {
                dtos.add(toDto(operatingHour));
            }
        }
        return dtos;
    }
}
