package com.sparta26.baemin.dto.operatinghours;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOperatingHoursDto {

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Opening time must be in HH:mm 24-hour format")
    private String opening_time;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Closing time must be in HH:mm 24-hour format")
    private String closing_time;

    @NotBlank
    @Pattern(regexp = "^(월요일|화요일|수요일|목요일|금요일|토요일|일요일)$",
            message = "Open days must be a single Korean weekday name (e.g., 월요일, 화요일, 수요일)")
    private String open_days;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Last order time must be in HH:mm 24-hour format")
    private String last_order;
}
