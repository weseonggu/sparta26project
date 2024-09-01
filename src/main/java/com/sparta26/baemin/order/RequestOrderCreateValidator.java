package com.sparta26.baemin.order;

import com.sparta26.baemin.dto.order.RequestOrderCreateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RequestOrderCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return RequestOrderCreateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestOrderCreateDto order = (RequestOrderCreateDto) target;

        if ("ONLINE".equals(order.getOrderType())) {
            ValidationUtils.rejectIfEmptyOrWhitespace(
                    errors,
                    "address",
                    "field.required",
                    "Address is required for online orders."
            );
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "orderType",
                "field.required",
                "Order type is required."
        );
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "storeId",
                "field.required",
                "Store ID is required."
        );
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "cardNumber",
                "field.required",
                "Card number is required."
        );

        if (order.getOrderProducts() == null || order.getOrderProducts().isEmpty()) {
            errors.rejectValue(
                    "orderProducts",
                    "field.required",
                    "At least one order product is required."
            );
        }
    }
}
