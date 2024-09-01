package com.sparta26.baemin.dto.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class RequestOrderUpdateDto {

    @NotEmpty(message = "Address is required.")
    private String address;
    private String orderRequest;
    private String deliveryRequest;
}
