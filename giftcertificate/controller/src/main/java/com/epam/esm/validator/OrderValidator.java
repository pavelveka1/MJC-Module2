package com.epam.esm.validator;

import com.epam.esm.service.dto.OrderDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class OrderValidator implements Validator {
    private static final String CERTIFICATES = "certificates";
    private static final String EMPTY_ORDER = "order.empty";

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, CERTIFICATES, EMPTY_ORDER);
    }
}
