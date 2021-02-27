package com.epam.esm.validator;

import com.epam.esm.service.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserDtoValidator {
    private static final String NAME_PATTERN = ".{2,20}";
    private static final String PASSWORD_PATTERN = ".{4,20}";

    public static boolean validate(UserDto userDto) {
        boolean result = true;
        if (Objects.nonNull(userDto.getUsername())) {
            if (!userDto.getUsername().matches(NAME_PATTERN)) {
                result = false;
            }
        } else {
            result = false;
        }
        if (Objects.nonNull(userDto.getPassword())) {
            if (!userDto.getPassword().matches(PASSWORD_PATTERN)) {
                result = false;
            }
        } else {
            result = false;
        }
        if (Objects.nonNull(userDto.getFirstName())) {
            if (!userDto.getFirstName().matches(NAME_PATTERN)) {
                result = false;
            }
        } else {
            result = false;
        }
        if (Objects.nonNull(userDto.getLastName())) {
            if (!userDto.getLastName().matches(NAME_PATTERN)) {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }
}
