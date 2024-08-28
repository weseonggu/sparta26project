package com.sparta26.baemin.dto.orderproduct;

import lombok.Getter;

@Getter
public class RequestOrderProductDto {
    private String productId;
    private Integer price;
    private Integer amount;

    public RequestOrderProductDto(String productId, Integer price, Integer amount) {
        this.productId = productId;
        this.price = price;
        this.amount = amount;
    }
}
