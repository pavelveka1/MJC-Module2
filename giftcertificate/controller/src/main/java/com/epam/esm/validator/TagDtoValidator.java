package com.epam.esm.validator;

import com.epam.esm.service.dto.TagDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TagDtoValidator implements Validator {
    private static final String NAME_PATTERN = ".{2,45}";
    private static final String NAME = "name";
    private static final String TAG_NAME_INCORRECT = "tag.name.incorrect";

    @Override
    public boolean supports(Class<?> clazz) {
        return TagDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TagDto tagDto = (TagDto) target;
        if (tagDto.getName() == null) {
            errors.rejectValue(NAME, TAG_NAME_INCORRECT);
        } else if (!tagDto.getName().matches(NAME_PATTERN)) {
            errors.rejectValue(NAME, TAG_NAME_INCORRECT);
        }
    }
}
