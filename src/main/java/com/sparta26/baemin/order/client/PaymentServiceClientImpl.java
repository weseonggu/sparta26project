package com.sparta26.baemin.order.client;

import com.sparta26.baemin.dto.payment.RequestPaymentDto;
import com.sparta26.baemin.dto.payment.ResponsePaymentInfoDto;
import com.sparta26.baemin.payment.entity.Payment;
import com.sparta26.baemin.payment.entity.PaymentStatus;
import com.sparta26.baemin.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceClientImpl implements PaymentServiceClient{

    private final PaymentService paymentService;

    @Override
    public Payment pay(String cardNumber, Integer totalPrice) {

        ResponsePaymentInfoDto responsePaymentInfoDto = paymentService.pay(
                RequestPaymentDto.createRequestPaymentDto(cardNumber, totalPrice));

        return convertToPayment(responsePaymentInfoDto);
    }

    @Override
    public Payment cancelPay(String paymentId, String cancel, String role, String email) {

        ResponsePaymentInfoDto responsePaymentInfoDto =
                paymentService.cancelPay(paymentId, cancel, role, email);

        return convertToPayment(responsePaymentInfoDto);
    }

    private Payment convertToPayment(ResponsePaymentInfoDto response) {
        return Payment.createPayment(
                PaymentStatus.fromString(response.getStatus()),
                response.getCardNumber(),
                response.getPayDate(),
                response.getTotalPrice()
        );
    }
}
