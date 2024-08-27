package com.sparta26.baemin.dto.operatinghours;

import com.sparta26.baemin.operatinghours.entity.OperatingHours;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseOperatingDto {

    private String operating_hours_id;
    private String opening_time;
    private String closing_time;
    private String open_days;
    private String last_order;



    public static ResponseOperatingDto toDto(OperatingHours savedOperating) {
        return new ResponseOperatingDto(savedOperating.getId().toString(),
                savedOperating.getOpeningTime(),
                savedOperating.getClosingTime(),
                savedOperating.getOpenDays(),
                savedOperating.getLastOrder());
    }
}
