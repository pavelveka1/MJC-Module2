package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.IdNotExistServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    /**
     * TagJDBCTemplate is used for operations with User
     */
    @Autowired
    private UserDAO userDAO;

    @Transactional
    @Override
    public User getUser(long id) throws IdNotExistServiceException {
        User user;
        user = userDAO.getUser(id);
        if (user == null) {
            throw new IdNotExistServiceException("User with id = " + id + " is not exist in DB");
        }
        return user;
    }

    @Transactional
    @Override
    public List<User> getUsers() {
        return userDAO.getUsers();
    }
}
