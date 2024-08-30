package com.sparta26.baemin.dto.payment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponsePaymentInfoDto {

    private String id;
    private String status;
    private String cardNumber;
    private LocalDateTime payDate;
    private Integer totalPrice;

    public static ResponsePaymentInfoDto createPaymentInfo(String id, String status, String cardNumber, LocalDateTime payDate, Integer totalPrice) {
        return new ResponsePaymentInfoDto(id, status, cardNumber, payDate, totalPrice);
    }
    public ResponsePaymentInfoDto(String id, String status, String cardNumber, LocalDateTime payDate, Integer totalPrice) {
        this.id = id;
        this.status = status;
        this.cardNumber = cardNumber;
        this.payDate = payDate;
        this.totalPrice = totalPrice;
    }
}
