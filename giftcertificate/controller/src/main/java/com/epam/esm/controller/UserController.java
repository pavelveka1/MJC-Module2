package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.IdNotExistServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/controller/api")
public class UserController {


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
    public List<User> getUsers() throws IdNotExistServiceException {
        List<User> users=userService.getUsers();
        for(User user:users){
            user.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
        }
        return users;
    }

}
