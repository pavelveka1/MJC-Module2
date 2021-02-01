package com.epam.esm.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class GiftCertificateDtoPatchValidator implements Validator {
    private static final String NAME_PATTERN = ".{2,45}";
    private static final String DESCRIPTION_PATTERN = ".{2,300}";
    private static final int MIN_PRICE = 0;
    private static final int MIN_DURATION = 0;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CERTIFICATE_NAME_INCORRECT = "certificate.name.incorrect";
    private static final String CERTIFICATE_DESCRIPTION_INCORRECT = "certificate.description.incorrect";
    private static final String CERTIFICATE_PRICE_INCORRECT = "certificate.price.incorrect";
    private static final String CERTIFICATE_DURATION_INCORRECT = "certificate.duration.incorrect";

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiftCertificateDto giftCertificateDto = (GiftCertificateDto) target;
        if (giftCertificateDto.getName() != null) {
            if (!giftCertificateDto.getName().matches(NAME_PATTERN)) {
                errors.rejectValue(NAME, CERTIFICATE_NAME_INCORRECT);
            }
        }
        if (giftCertificateDto.getDescription() != null) {
            if (!giftCertificateDto.getDescription().matches(DESCRIPTION_PATTERN)) {
                errors.rejectValue(DESCRIPTION, CERTIFICATE_DESCRIPTION_INCORRECT);
            }
        }
        if (giftCertificateDto.getPrice() != null) {
            if (giftCertificateDto.getPrice() <= MIN_PRICE) {
                errors.rejectValue(PRICE, CERTIFICATE_PRICE_INCORRECT);
            }
        }
        if (giftCertificateDto.getDuration() != null) {
            if (giftCertificateDto.getDuration() <= MIN_DURATION) {
                errors.rejectValue(DURATION, CERTIFICATE_DURATION_INCORRECT);
            }
        }
    }

}
