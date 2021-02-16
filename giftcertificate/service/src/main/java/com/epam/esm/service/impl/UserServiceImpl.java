package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import com.epam.esm.service.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final String KEY_USER_ID_NOT_EXIST = "user_id_not_exist";
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
    @Override
    public User getUser(long id) throws IdNotExistServiceException {
        User user;
        user = userDAO.getUser(id);
        if (Objects.isNull(user)) {
            throw new IdNotExistServiceException(KEY_USER_ID_NOT_EXIST);
        }
        return user;
    }

    /**
     * Read users from DB
     *
     * @param page number of page
     * @param size size of page
     * @return List of users
     * @throws PaginationException if page equals zero
     */
    @Override
    public List<User> getUsers(Integer page, Integer size) throws PaginationException {
        page = PaginationUtil.checkPage(page);
        size = PaginationUtil.checkSizePage(size);
        return userDAO.getUsers(page, size);
    }
}
