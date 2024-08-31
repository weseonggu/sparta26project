package com.sparta26.baemin.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAddressDto {

    @NotNull(message = "우편번호를 입력해주세요")
    @NotBlank(message = "우편번호를 입력해주세요")
    private String zipCode;

    @NotNull(message = "도로명 주소를 입력하세요")
    @NotBlank(message = "도로명 주소를 입력하세요")
    private String roadAddress;

    @NotNull(message = "영문 도로명 주소를 입력하세요")
    @NotBlank(message = "영문 도로명 주소를 입력하세요")
    private String roadAddressEnglish;
}
