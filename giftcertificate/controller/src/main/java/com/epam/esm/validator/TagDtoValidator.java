package com.epam.esm.validator;

import com.epam.esm.constant.ControllerConstant;
import com.epam.esm.service.dto.TagDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TagDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TagDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TagDto tagDto = (TagDto) target;
        ValidationUtils.rejectIfEmpty(errors, ControllerConstant.NAME, ControllerConstant.TAG_NAME_INCORRECT);
        if (!errors.hasErrors()) {
            if (!tagDto.getName().matches(ControllerConstant.NAME_PATTERN)) {
                errors.rejectValue(ControllerConstant.NAME, ControllerConstant.TAG_NAME_INCORRECT);
            }
        }

    }
}
