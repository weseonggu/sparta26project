package com.sparta26.baemin.order.entity;

import lombok.Getter;

@Getter
public enum OrderType {
// 온라인주문, 매장주문
    ONLINE, OFFLINE;

    public static OrderType fromString(String value) {
        for (OrderType type : OrderType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown order type: " + value);
    }
}
