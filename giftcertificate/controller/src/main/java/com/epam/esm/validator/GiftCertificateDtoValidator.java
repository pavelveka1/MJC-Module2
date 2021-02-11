package com.epam.esm.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class GiftCertificateDtoValidator implements Validator {
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
        return GiftCertificateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiftCertificateDto giftCertificateDto = (GiftCertificateDto) target;
        ValidationUtils.rejectIfEmpty(errors, NAME, CERTIFICATE_NAME_INCORRECT);
        ValidationUtils.rejectIfEmpty(errors, DESCRIPTION, CERTIFICATE_DESCRIPTION_INCORRECT);
       if(!errors.hasErrors()){
           if (!giftCertificateDto.getName().matches(NAME_PATTERN)) {
               errors.rejectValue(NAME, CERTIFICATE_NAME_INCORRECT);
           } else if (!giftCertificateDto.getDescription().matches(DESCRIPTION_PATTERN)) {
               errors.rejectValue(DESCRIPTION, CERTIFICATE_DESCRIPTION_INCORRECT);
           } else if (giftCertificateDto.getPrice() <= MIN_PRICE) {
               errors.rejectValue(PRICE, CERTIFICATE_PRICE_INCORRECT);
           } else if (giftCertificateDto.getDuration() <= MIN_DURATION) {
               errors.rejectValue(DURATION, CERTIFICATE_DURATION_INCORRECT);
           }
       }
    }
}
