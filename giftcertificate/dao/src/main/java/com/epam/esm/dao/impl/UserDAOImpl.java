package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    /**
     * Instance of SessionFactory for work with DB
     */
    @Autowired
    private SessionFactory sessionFactory;

    private static final String SELECT_USER_BY_ID = "User.findById";
    private static final String SELECT_ALL_USERS = "User.findAll";
    private static final String ID = "id";

    @Override
    public User getUser(long id) throws EmptyResultDataAccessException {
        return (User) sessionFactory.getCurrentSession().getNamedQuery(SELECT_USER_BY_ID).setParameter(ID, id).uniqueResult();
    }

    @Override
    public List<User> getUsers() {
        return sessionFactory.getCurrentSession().getNamedQuery(SELECT_ALL_USERS).list();
    }
}
