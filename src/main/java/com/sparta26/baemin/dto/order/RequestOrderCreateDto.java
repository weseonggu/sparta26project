package com.sparta26.baemin.dto.order;

import com.sparta26.baemin.dto.orderproduct.RequestOrderProductDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RequestOrderCreateDto {

    private String address;
    private String orderType;
    private String orderRequest;
    private String deliveryRequest;
    private String storeId;
    private String cardNumber;
    private List<RequestOrderProductDto> orderProducts;

    public RequestOrderCreateDto(String address, String orderType, String orderRequest, String deliveryRequest, String storeId, String cardNumber, List<RequestOrderProductDto> orderProducts) {
        this.address = address;
        this.orderType = orderType;
        this.orderRequest = orderRequest;
        this.deliveryRequest = deliveryRequest;
        this.storeId = storeId;
        this.cardNumber = cardNumber;
        this.orderProducts = orderProducts;
    }
}
