package com.sparta26.baemin.dto.deliveryzone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDeliveryDto {

    @NotBlank(message = "배달 가능 지역을 작성해주세요 (예: 종로구 효자로)")
    @Pattern(regexp = "^[가-힣]+( [가-힣]+)*$", message = "유효하지 않은 형식입니다. 예: 종로구 효자로")
    private String name;
}
