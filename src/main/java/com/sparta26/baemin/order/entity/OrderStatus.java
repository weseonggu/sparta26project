package com.sparta26.baemin.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING,
    CONFIRM,
    COMPLETE,
    CANCEL;

    public static OrderStatus fromString(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown order status: " + value);
    }
}
