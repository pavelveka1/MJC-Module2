package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;

import java.util.List;

/**
 * Interface UserService.
 * Contains methods for work with Tag class
 */
public interface UserService {

    /**
     * Get user by id
     *
     * @param id id of user
     * @return User
     * @throws IdNotExistServiceException if user with passed id is not exist
     */
    User getUser(long id) throws IdNotExistServiceException;

    /**
     * Get users
     *
     * @param page number of page
     * @param size size of page
     * @return List of users
     * @throws PaginationException if page equals zero
     */
    List<User> getUsers(Integer page, Integer size) throws PaginationException;

    User register(UserDto user);

    User findByUserName(String userName);
}
