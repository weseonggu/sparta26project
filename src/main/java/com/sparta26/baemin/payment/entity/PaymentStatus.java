package com.sparta26.baemin.payment.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING, COMPLETE, CANCEL;
    public static PaymentStatus fromString(String value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown payment status: " + value);
    }
}
