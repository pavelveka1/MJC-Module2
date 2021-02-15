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

    private static final String KEY_PAGINATION = "pagination";
    private static final String KEY_USER_ID_NOT_EXIST = "user_id_not_exist";
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
    public User getUser(long id, String language) throws IdNotExistServiceException {
        User user;
        user = userDAO.getUser(id);
        if (user == null) {
            throw new IdNotExistServiceException(KEY_USER_ID_NOT_EXIST, language);
        }
        return user;
    }

    @Transactional
    @Override
    public List<User> getUsers(Integer page, Integer size, String language) throws PaginationException {
        page = checkPage(page, language);
        size = checkSizePage(size);
        return userDAO.getUsers(page, size);
    }

    private Integer checkPage(Integer page, String language) throws PaginationException {
        if (page < ONE) {
            if (page == ZERO) {
                throw new PaginationException(KEY_PAGINATION, language);
            }
            page = Math.abs(page);
        }
        return page;
    }

    private Integer checkSizePage(Integer size) throws PaginationException {
        if (size < ONE) {
            size = Math.abs(size);
        }
        return size;
    }
}
