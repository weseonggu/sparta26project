package com.sparta26.baemin.dto.order;

import lombok.Getter;

@Getter
public class ResponseOrderCreateDto {

    private String orderId;
    private String storeId;
    private Integer totalPrice;
    private String orderStatus;

    public static ResponseOrderCreateDto createResponseOrderCreateDto(String orderId, String storeId, Integer totalPrice, String orderStatus) {
        return new ResponseOrderCreateDto(orderId, storeId, totalPrice, orderStatus);
    }

    public ResponseOrderCreateDto(String orderId, String storeId, Integer totalPrice, String orderStatus) {
        this.orderId = orderId;
        this.storeId = storeId;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }
}
