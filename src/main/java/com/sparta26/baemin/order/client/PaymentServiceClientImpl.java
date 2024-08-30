package com.sparta26.baemin.order.client;

import com.sparta26.baemin.dto.payment.RequestPaymentDto;
import com.sparta26.baemin.dto.payment.ResponsePaymentInfoDto;
import com.sparta26.baemin.order.entity.Order;
import com.sparta26.baemin.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceClientImpl implements PaymentServiceClient{

    private final PaymentService paymentService;

    @Override
    public ResponsePaymentInfoDto pay(String cardNumber, Integer totalPrice) {
        return paymentService.pay(
                RequestPaymentDto.createRequestPaymentDto(cardNumber, totalPrice)
        );
    }
}
