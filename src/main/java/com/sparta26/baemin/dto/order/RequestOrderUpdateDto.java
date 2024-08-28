package com.sparta26.baemin.dto.order;

import lombok.Getter;

@Getter
public class RequestOrderUpdateDto {

    private boolean cancelRequest;
    private String newStatus;
}
