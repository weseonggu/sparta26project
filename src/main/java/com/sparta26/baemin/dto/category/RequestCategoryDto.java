package com.sparta26.baemin.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCategoryDto {

    @NotBlank(message = "가게의 카테고리는 필수로 입력되어야 합니다.")
    @Pattern(regexp = "^(한식|중식|분식|치킨|피자)$",
            message = "category must be a Korean name (e.g., 한식, 중식, 분식, 치킨, 피자)")
    private String name;
}
