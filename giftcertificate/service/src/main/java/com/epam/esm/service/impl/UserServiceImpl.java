package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String KEY_USER_ID_NOT_EXIST = "user_id_not_exist";
    private static final String USER_ROLE = "ROLE_USER";
    private static final String ADMIN_AUTHORITY = "ROLE_ADMIN";
    private static final String FORBIDDEN="get_user_forbidden";

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
       if(RoleUtil.hasAdminAuthority(jwtUser)){
           user = userDAO.findById(id);
       }else{
           if(id==jwtUser.getId()){
               user=userDAO.findById(jwtUser.getId());
           }else {
               throw new JwtAuthenticationException(FORBIDDEN);
           }
       }
        if (!user.isPresent()) {
            throw new IdNotExistServiceException(KEY_USER_ID_NOT_EXIST);
        }
        return user.get();
    }


    @Override
    @Secured("ROLE_ADMIN")
    public List<User> getUsers(Integer page, Integer size) throws PaginationException {
        page = PaginationUtil.checkPage(page);
        size = PaginationUtil.checkSizePage(size);
        Pageable pageable = PageRequest.of(page - 1, size);
        return userDAO.findAll(pageable).getContent();
    }

    @Override
    @Transactional
    public User register(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
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
