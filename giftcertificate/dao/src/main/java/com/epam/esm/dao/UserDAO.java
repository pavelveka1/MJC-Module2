package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;

public interface UserDAO {

    User getUser(long id);

    List<User> getUsers(Integer page, Integer size);

}
