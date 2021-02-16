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

@RestController
@RequestMapping("/api")
public class UserController {

    private static final String DEFAULT_PAGE_SIZE = "1000";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    /**
     * OrderService is used for work with Orders
     */
    @Autowired
    private UserService userService;

    /**
     * Method gets user by id
     *
     * @param id id of user
     * @return User
     * @throws IdNotExistServiceException if user with such id is not exist in DB
     */
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable @Min(1) long id) throws IdNotExistServiceException {
        User user = userService.getUser(id);
        HATEOASBuilder.addLinksToUser(user);
        return user;
    }

    /**
     * Method gets users
     *
     * @param page number of page
     * @param size number of entity on page
     * @return List of User
     * @throws IdNotExistServiceException can be thrown by HATEOASBuilder while reading by id
     * @throws PaginationException        if page number equals zero
     */
    @GetMapping("/users")
    public List<User> getUsers(@RequestParam( defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                               @RequestParam( defaultValue = DEFAULT_PAGE_SIZE) Integer size)
            throws IdNotExistServiceException, PaginationException {
        List<User> users = userService.getUsers(page, size);
        HATEOASBuilder.addLinksToUsers(users);
        return users;
    }
}
