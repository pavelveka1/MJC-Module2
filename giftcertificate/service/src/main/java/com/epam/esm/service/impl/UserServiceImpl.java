package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import com.epam.esm.service.util.PaginationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String KEY_USER_ID_NOT_EXIST = "user_id_not_exist";
    private static final String USER_ROLE = "ROLE_USER";
    /**
     * TagJDBCTemplate is used for operations with User
     */
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Read user by id
     *
     * @param id id of user
     * @return User
     * @throws IdNotExistServiceException if user with passed id is not exist
     */
    @Override
    public User getUser(long id) throws IdNotExistServiceException {
        Optional<User> user;
        user = userDAO.findById(id);
        if (!user.isPresent()) {
            throw new IdNotExistServiceException(KEY_USER_ID_NOT_EXIST);
        }
        return user.get();
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
    @Secured( "ROLE_ADMIN")
    public List<User> getUsers(Integer page, Integer size) throws PaginationException {
        page = PaginationUtil.checkPage(page);
        size = PaginationUtil.checkSizePage(size);
        Pageable pageable= PageRequest.of(page-1, size);
        return  userDAO.findAll(pageable).getContent();
    }

    @Override
    @Transactional
    public User register(UserDto userDto) {
        User user=modelMapper.map(userDto, User.class);
        Role role = roleDAO.findRoleByName(USER_ROLE);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        User reggisteredUser = userDAO.save(user);
        //duplicate
        return reggisteredUser;
    }

    @Transactional
    @Override
    public User findByUserName(String userName) {
        return userDAO.findByUsername(userName);
    }

}
