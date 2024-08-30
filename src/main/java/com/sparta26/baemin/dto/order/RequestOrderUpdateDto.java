package com.sparta26.baemin.dto.order;

import lombok.Getter;

@Getter
public class RequestOrderUpdateDto {

    private String address;
    private String orderRequest;
    private String deliveryRequest;
    private boolean cancelRequest;
    private String newStatus;
}
