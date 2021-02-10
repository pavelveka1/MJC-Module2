package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;

import java.util.List;

public interface UserService {

    User getUser(long id) throws IdNotExistServiceException;

    List<User> getUsers(Integer page, Integer size) throws PaginationException;
}
