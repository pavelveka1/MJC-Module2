package com.epam.esm.controller;

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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationRestController {

    private static final String USER_NAME = "username";
    private static final String TOKEN = "token";
    private static final String DUPLICATE_USERNAME = "duplicate_username";
    private static final String USER_NOT_FOUND = "user_not_found";
    private static final String AUTHENTICATION_EXCEPTION = "authentication_exception";
    private static final String USER_DTO_NOT_VALID = "user_not_valid";

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    /*
        @Autowired
        private Validator userDtoValidator;

        @InitBinder
        protected void initBinder(WebDataBinder binder) {
            binder.setValidator(userDtoValidator);
        }


     */
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
            response.put(TOKEN, token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(AUTHENTICATION_EXCEPTION);
        }
    }

    @PostMapping("signUp")
    public ResponseEntity signUp(@RequestBody UserDto userDto) throws UserRegistrationException, ValidationException {
        if (!UserDtoValidator.validate(userDto)) {
            throw new ValidationException(USER_DTO_NOT_VALID);
        }
        String username = userDto.getUsername();
        User user = userService.findByUserName(username);
        if (Objects.nonNull(user)) {
            throw new UserRegistrationException(DUPLICATE_USERNAME);
        }
        user = userService.register(userDto);
        return ResponseEntity.ok(user);
    }
}
