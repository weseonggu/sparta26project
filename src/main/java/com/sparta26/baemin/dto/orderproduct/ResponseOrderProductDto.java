package com.sparta26.baemin.dto.orderproduct;

import lombok.Getter;

@Getter
public class ResponseOrderProductDto {
    private String productId;
    private String name;
    private Integer price;
    private Integer amount;

    public static ResponseOrderProductDto createResponseOrderProductDto(String productId, String name, Integer price, Integer amount) {
        return new ResponseOrderProductDto(productId, name, price, amount);
    }

    public ResponseOrderProductDto(String productId, String name, Integer price, Integer amount) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }
}
