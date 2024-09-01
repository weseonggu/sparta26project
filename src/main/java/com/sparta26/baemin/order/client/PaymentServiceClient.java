package com.sparta26.baemin.order.client;

import com.sparta26.baemin.payment.entity.Payment;

public interface PaymentServiceClient {
    Payment pay(String cardNumber, Integer totalPrice);
    Payment cancelPay(String paymentId, String cancel, String role, String email);
}
