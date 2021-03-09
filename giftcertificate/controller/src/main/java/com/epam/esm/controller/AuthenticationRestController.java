package com.epam.esm.controller;

import com.epam.esm.constant.ControllerConstant;
import com.epam.esm.entity.User;
import com.epam.esm.exceptionhandler.ValidationException;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.AuthenticationRequestDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.UserRegistrationException;
import com.epam.esm.service.security.JwtTokenProvider;
import com.epam.esm.validator.UserDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            String password = requestDto.getPassword();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authenticationToken);
            User user = userService.findByUserName(username);
            String token = jwtTokenProvider.createToken(username, user.getRoles());
            Map<Object, Object> response = new HashMap<>();
            response.put(ControllerConstant.TOKEN, token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(ControllerConstant.AUTHENTICATION_EXCEPTION);
        }
    }

    @PostMapping("signUp")
    public ResponseEntity signUp(@RequestBody UserDto userDto) throws UserRegistrationException, ValidationException {
        if (!UserDtoValidator.validate(userDto)) {
            throw new ValidationException(ControllerConstant.USER_DTO_NOT_VALID);
        }
        String username = userDto.getUsername();
        User user = userService.findByUserName(username);
        if (Objects.nonNull(user)) {
            throw new UserRegistrationException(ControllerConstant.DUPLICATE_USERNAME);
        }
        user = userService.register(userDto);
        return ResponseEntity.ok(user);
    }
}
