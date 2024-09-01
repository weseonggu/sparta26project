package com.sparta26.baemin.dto.payment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponsePaymentInfoDto {

    private String status;
    private String cardNumber;
    private LocalDateTime payDate;
    private Integer totalPrice;

    public static ResponsePaymentInfoDto createPaymentInfo(String status, String cardNumber, LocalDateTime payDate, Integer totalPrice) {
        return new ResponsePaymentInfoDto(status, cardNumber, payDate, totalPrice);
    }
    public ResponsePaymentInfoDto(String status, String cardNumber, LocalDateTime payDate, Integer totalPrice) {
        this.status = status;
        this.cardNumber = cardNumber;
        this.payDate = payDate;
        this.totalPrice = totalPrice;
    }
}
