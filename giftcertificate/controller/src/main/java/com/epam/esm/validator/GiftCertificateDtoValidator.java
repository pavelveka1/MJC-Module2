package com.epam.esm.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
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
        if (!giftCertificateDto.getName().matches(".{2,45}")) {
            errors.rejectValue("name", "certificate.name.incorrect");
        } else if (!giftCertificateDto.getDescription().matches(".{2,300}")) {
            errors.rejectValue("description", "certificate.description.incorrect");
        } else if (giftCertificateDto.getPrice() <= 0) {
            errors.rejectValue("price", "certificate.price.incorrect");
        } else if (giftCertificateDto.getDuration() <= 0) {
            errors.rejectValue("duration", "certificate.duration.incorrect");
        }
    }
}
