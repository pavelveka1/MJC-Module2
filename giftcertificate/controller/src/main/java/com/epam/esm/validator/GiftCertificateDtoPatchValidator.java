package com.epam.esm.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GiftCertificateDtoPatchValidator {
    private static final String NAME_PATTERN = ".{2,45}";
    private static final String DESCRIPTION_PATTERN = ".{2,300}";
    private static final int MIN_PRICE = 0;
    private static final int MIN_DURATION = 0;

    public static boolean validate(GiftCertificateDto giftCertificateDto) {
        boolean result = true;
        if (Objects.nonNull(giftCertificateDto.getName())) {
            if (!giftCertificateDto.getName().matches(NAME_PATTERN)) {
                result = false;
            }
        }
        if (Objects.nonNull(giftCertificateDto.getDescription())) {
            if (!giftCertificateDto.getDescription().matches(DESCRIPTION_PATTERN)) {
                result = false;
            }
        }
        if (Objects.nonNull(giftCertificateDto.getPrice())) {
            if (giftCertificateDto.getPrice() <= MIN_PRICE ) {
                result = false;
            }
        }
        if (Objects.nonNull(giftCertificateDto.getDuration())) {
            if (giftCertificateDto.getDuration() <= MIN_DURATION) {
                result = false;
            }
        }
        return result;
    }

}
