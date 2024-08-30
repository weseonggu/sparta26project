package com.sparta26.baemin.order.client;

import com.sparta26.baemin.dto.payment.ResponsePaymentInfoDto;

public interface PaymentServiceClient {
    public ResponsePaymentInfoDto pay(String cardNumber, Integer totalPrice);
}
