package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    /**
     * Instance of SessionFactory for work with DB
     */
    @Autowired
    private SessionFactory sessionFactory;

    private static final String SELECT_USER_BY_ID = "User.findById";
    private static final String ID = "id";
    private static final int ONE = 1;

    /**
     * Get User by id
     *
     * @param id id of user
     * @return User
     * @throws EmptyResultDataAccessException if user with such id is not exist
     */
    @Override
    public User getUser(long id) throws EmptyResultDataAccessException {
        return (User) sessionFactory.getCurrentSession().getNamedQuery(SELECT_USER_BY_ID).setParameter(ID, id).uniqueResult();
    }

    /**
     * Get users
     *
     * @param page number of page
     * @param size size of page
     * @return List of User
     */
    @Override
    public List<User> getUsers(Integer page, Integer size) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<User> cr = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = cr.from(User.class);
        cr.select(userRoot).orderBy(criteriaBuilder.asc(userRoot.get(ID)));
        Query<User> query = getSession().createQuery(cr);
        query.setFirstResult((page - ONE) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
