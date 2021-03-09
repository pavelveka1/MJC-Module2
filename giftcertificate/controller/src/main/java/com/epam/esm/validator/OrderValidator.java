package com.epam.esm.validator;

import com.epam.esm.constant.ControllerConstant;
import com.epam.esm.service.dto.OrderDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class OrderValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderDto orderDto = (OrderDto) target;
        ValidationUtils.rejectIfEmpty(errors, ControllerConstant.CERTIFICATES, ControllerConstant.EMPTY_ORDER);
        if (!errors.hasErrors()) {
            if (orderDto.getCertificates().size() == ControllerConstant.ZERO) {
                errors.rejectValue(ControllerConstant.CERTIFICATES, ControllerConstant.EMPTY_ORDER);
            }
        }
    }
}
