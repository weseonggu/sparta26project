package com.sparta26.baemin.dto.store;

import com.sparta26.baemin.dto.operatinghours.RequestOperatingHoursDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestStoreDto {

    @NotBlank(message = "가게 이름은 필수 입니다.")
    private String name;
    private String description;

    @NotBlank(message = "가게 주소는 필수 입니다.")
    private String address;
    @NotBlank(message = "가게 번호는 필수 입니다.")
    private String phone_number;

    @NotEmpty(message = "운영 시간은 최소 하나 이상 필수 입니다.")
    @Valid
    private List<RequestOperatingHoursDto> operating_hours;


}
