package com.sparta26.baemin.dto.order;

import com.sparta26.baemin.dto.orderproduct.ResponseOrderProductDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseOrderDto {

    private String orderId;
    private String address;
    private String orderType;
    private String orderRequest;
    private String deliveryRequest;
    private Integer totalPrice;
    private String storeId;
    List<ResponseOrderProductDto> orderProducts;
    private String paymentId;
    private String status;

    public static ResponseOrderDto createResponseOrderDto(String orderId, String address, String orderType, String orderRequest, String deliveryRequest, Integer totalPrice, String storeId, List<ResponseOrderProductDto> orderProducts, String paymentId, String status) {
        return new ResponseOrderDto(orderId, address, orderType, orderRequest, deliveryRequest, totalPrice, storeId, orderProducts, paymentId, status);
    }

    public ResponseOrderDto(String orderId, String address, String orderType, String orderRequest, String deliveryRequest, Integer totalPrice, String storeId, List<ResponseOrderProductDto> orderProducts, String paymentId, String status) {
        this.orderId = orderId;
        this.address = address;
        this.orderType = orderType;
        this.orderRequest = orderRequest;
        this.deliveryRequest = deliveryRequest;
        this.totalPrice = totalPrice;
        this.storeId = storeId;
        this.orderProducts = orderProducts;
        this.paymentId = paymentId;
        this.status = status;
    }
}
