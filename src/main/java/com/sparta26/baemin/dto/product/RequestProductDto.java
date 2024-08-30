package com.sparta26.baemin.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestProductDto {

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String name;
    private String description;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 1, message = "가격은 1 이상의 값이어야 합니다.")
    private Integer price;

    @NotNull(message = "재고 수량은 필수입니다.")
    @Min(value = 1, message = "재고 수량은 1 이상의 값이어야 합니다.")
    private Integer stock_quantity;

    private String category;
    private String imageUrl;

}
