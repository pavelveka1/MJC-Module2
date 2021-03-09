package com.epam.esm.validator;

import com.epam.esm.constant.ControllerConstant;
import com.epam.esm.service.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserDtoValidator {

    public static boolean validate(UserDto userDto) {
        boolean result = true;
        if (Objects.nonNull(userDto.getUsername()) && Objects.nonNull(userDto.getPassword()) &&
                Objects.nonNull(userDto.getFirstName()) && Objects.nonNull(userDto.getLastName())) {
            if (!userDto.getUsername().matches(ControllerConstant.USER_NAME_PATTERN) ||
                    !userDto.getPassword().matches(ControllerConstant.PASSWORD_PATTERN) ||
                    !userDto.getFirstName().matches(ControllerConstant.USER_NAME_PATTERN) ||
                    !userDto.getLastName().matches(ControllerConstant.USER_NAME_PATTERN)) {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }
}
