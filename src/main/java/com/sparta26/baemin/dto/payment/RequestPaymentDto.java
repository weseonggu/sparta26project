package com.sparta26.baemin.dto.payment;

import lombok.Getter;

@Getter
public class RequestPaymentDto {

    private String cardNumber;
    private Integer totalPrice;

    public static RequestPaymentDto createRequestPaymentDto(String cardNumber, Integer totalPrice) {
        return new RequestPaymentDto(cardNumber, totalPrice);
    }

    public RequestPaymentDto(String cardNumber, Integer totalPrice) {
        this.cardNumber = cardNumber;
        this.totalPrice = totalPrice;
    }
}
