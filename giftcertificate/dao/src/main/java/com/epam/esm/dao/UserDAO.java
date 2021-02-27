package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;

/**
 * Interface UserDAO.
 * Contains methods for work with User class
 */
public interface UserDAO {

    /**
     * Get user by id
     *
     * @param id id of user
     * @return User
     */
    User getUser(long id);

    /**
     * Get users
     *
     * @param page number of page
     * @param size size of page
     * @return List of users
     */
    List<User> getUsers(Integer page, Integer size);

    User findByUserName(String username);

    User save(User user);

}
