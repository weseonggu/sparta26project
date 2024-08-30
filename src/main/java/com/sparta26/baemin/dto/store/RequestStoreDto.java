package com.sparta26.baemin.dto.store;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
