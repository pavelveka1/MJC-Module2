package com.epam.esm.validator;

import com.epam.esm.constant.ControllerConstant;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GiftCertificateDtoPatchValidator {

    public static boolean validate(GiftCertificateDto giftCertificateDto) {
        boolean result = true;
        if (Objects.nonNull(giftCertificateDto.getName())) {
            if (!giftCertificateDto.getName().matches(ControllerConstant.NAME_PATTERN)) {
                result = false;
            }
        }
        if (Objects.nonNull(giftCertificateDto.getDescription())) {
            if (!giftCertificateDto.getDescription().matches(ControllerConstant.DESCRIPTION_PATTERN)) {
                result = false;
            }
        }
        if (Objects.nonNull(giftCertificateDto.getPrice())) {
            if (giftCertificateDto.getPrice() <= ControllerConstant.MIN_PRICE) {
                result = false;
            }
        }
        if (Objects.nonNull(giftCertificateDto.getDuration())) {
            if (giftCertificateDto.getDuration() <= ControllerConstant.MIN_DURATION) {
                result = false;
            }
        }
        return result;
    }

}
