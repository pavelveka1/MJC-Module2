package com.epam.esm.validator;

import com.epam.esm.constant.ControllerConstant;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class GiftCertificateDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return GiftCertificateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiftCertificateDto giftCertificateDto = (GiftCertificateDto) target;
        ValidationUtils.rejectIfEmpty(errors, ControllerConstant.NAME, ControllerConstant.CERTIFICATE_NAME_INCORRECT);
        ValidationUtils.rejectIfEmpty(errors, ControllerConstant.DESCRIPTION, ControllerConstant.CERTIFICATE_DESCRIPTION_INCORRECT);
        ValidationUtils.rejectIfEmpty(errors, ControllerConstant.PRICE, ControllerConstant.CERTIFICATE_PRICE_INCORRECT);
        ValidationUtils.rejectIfEmpty(errors, ControllerConstant.DURATION, ControllerConstant.CERTIFICATE_DURATION_INCORRECT);
        if (!errors.hasErrors()) {
            if (!giftCertificateDto.getName().matches(ControllerConstant.NAME_PATTERN)) {
                errors.rejectValue(ControllerConstant.NAME, ControllerConstant.CERTIFICATE_NAME_INCORRECT);
            } else if (!giftCertificateDto.getDescription().matches(ControllerConstant.DESCRIPTION_PATTERN)) {
                errors.rejectValue(ControllerConstant.DESCRIPTION, ControllerConstant.CERTIFICATE_DESCRIPTION_INCORRECT);
            } else if (giftCertificateDto.getPrice() <= ControllerConstant.MIN_PRICE) {
                errors.rejectValue(ControllerConstant.PRICE, ControllerConstant.CERTIFICATE_PRICE_INCORRECT);
            } else if (giftCertificateDto.getDuration() <= ControllerConstant.MIN_DURATION) {
                errors.rejectValue(ControllerConstant.DURATION, ControllerConstant.CERTIFICATE_DURATION_INCORRECT);
            }
        }
    }
}
