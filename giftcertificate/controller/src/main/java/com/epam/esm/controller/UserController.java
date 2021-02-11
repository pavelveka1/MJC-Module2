package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/controller/api")
public class UserController {

    private static final String DEFAULT_PAGE_SIZE = "1000";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private static final Logger logger = Logger.getLogger(UserController.class);
    /**
     * OrderService is used for work with Orders
     */
    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable @Min(1) long id) throws IdNotExistServiceException {
        User user = userService.getUser(id);
        user.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(required = true, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                               @RequestParam(required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size)
            throws IdNotExistServiceException, PaginationException {
        List<User> users = userService.getUsers(page, size);
        for (User user : users) {
            user.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
        }
        return users;
    }

}
