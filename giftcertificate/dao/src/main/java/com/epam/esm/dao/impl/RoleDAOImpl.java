package com.epam.esm.dao.impl;

import com.epam.esm.dao.RoleDAO;
import com.epam.esm.entity.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOImpl implements RoleDAO {

    private static final String SELECT_ROLE_BY_NAME = "Role.findByName";
    private static final String NAME = "name";

    /**
     * Instance of SessionFactory for work with DB
     */
    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public Role getRoleByName(String roleName) {
        return (Role) getSession().getNamedQuery(SELECT_ROLE_BY_NAME).setParameter(NAME, roleName).uniqueResult();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
