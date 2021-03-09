package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.constant.ServiceConstant;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.JwtAuthenticationException;
import com.epam.esm.service.exception.PaginationException;
import com.epam.esm.service.security.JwtUser;
import com.epam.esm.service.util.PaginationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Secured("ROLE_USER")
    public User getUser(long id) throws IdNotExistServiceException {
        Optional<User> user;
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (RoleUtil.hasAdminAuthority(jwtUser)) {
            user = userDAO.findById(id);
        } else {
            if (id == jwtUser.getId()) {
                user = userDAO.findById(jwtUser.getId());
            } else {
                throw new JwtAuthenticationException(ServiceConstant.USER_FORBIDDEN);
            }
        }
        if (!user.isPresent()) {
            throw new IdNotExistServiceException(ServiceConstant.KEY_USER_ID_NOT_EXIST);
        }
        return user.get();
    }


    @Override
    @Secured("ROLE_ADMIN")
    public List<User> getUsers(Integer page, Integer size) throws PaginationException {
        int checkedPage = PaginationUtil.checkPage(page);
        int checkedSize = PaginationUtil.checkSizePage(size);
        Pageable pageable = PageRequest.of(checkedPage - 1, checkedSize);
        return userDAO.findAll(pageable).getContent();
    }

    @Override
    @Transactional
    public User register(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Role role = roleDAO.findRoleByName(ServiceConstant.USER_ROLE);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        return userDAO.save(user);
    }

    @Transactional
    @Override
    public User findByUserName(String userName) {
        return userDAO.findByUsername(userName);
    }
}
