package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {

    /**
     * Instance of SessionFactory for work with DB
     */
    @Autowired
    private SessionFactory sessionFactory;

    private static final String ID = "orders_id";
    private static final String ID_PARAM = "id";
    private static final String SELECT_ORDER_BY_ID = "Order.findById";
    private static final int ONE = 1;
    private static final String USER = "user";
    private static final Logger logger = Logger.getLogger(OrderDAOImpl.class);

    @Override
    public long makeOrder(Order order) {
        sessionFactory.getCurrentSession().saveOrUpdate(order);
        return order.getOrders_id();
    }

    /**
     *  Ger order by id of user
     * @param user orders of this user will be returned
     * @param page number of page
     * @param size size of page
     * @return List of Orders
     */
    @Override
    public List<Order> getOrdersByUserId(User user, Integer page, Integer size) {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Order> cr = cb.createQuery(Order.class);
        Root<Order> orderRoot = cr.from(Order.class);
        cr.select(orderRoot).where(cb.equal(orderRoot.get(USER), user)).orderBy(cb.asc(orderRoot.get(ID)));
        Query<Order> query = getSession().createQuery(cr);
        query.setFirstResult((page - ONE) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }


    @Override
    public Order getOrder(long id) {
        return (Order) sessionFactory.getCurrentSession().getNamedQuery(SELECT_ORDER_BY_ID).setParameter(ID_PARAM, id).uniqueResult();

    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
