package com.epam.esm.validator;

import com.epam.esm.service.dto.TagDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
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
        if (!tagDto.getName().matches(".{2,45}")) {
            errors.rejectValue("name", "tag.name.incorrect");
        }
    }
}
