package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final int ZERO = 0;
    private static final int ONE = 1;
    /**
     * TagJDBCTemplate is used for operations with User
     */
    @Autowired
    private UserDAO userDAO;

    /**
     * Read user by id
     *
     * @param id id of user
     * @return User
     * @throws IdNotExistServiceException if user with passed id is not exist
     */
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
    public List<User> getUsers(Integer page, Integer size) throws PaginationException {
        checkPageAndSize(page, size);
        return userDAO.getUsers(page, size);
    }

    private void checkPageAndSize(Integer page, Integer size) throws PaginationException {
        if (page < ONE) {
            if (page == ZERO) {
                throw new PaginationException("It's imposible to get page with zero number");
            }
            page = Math.abs(page);
        }
        if (size < ONE) {
            size = Math.abs(size);
        }
    }
}
