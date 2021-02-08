package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.entity.Order;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {

    /**
     * Instance of SessionFactory for work with DB
     */
    @Autowired
    private SessionFactory sessionFactory;

    private static final String ID = "id";
    private static final String SELECT_ORDER_BY_ID = "Order.findById";
    private static final String GET_ORDERS_BY_USER_ID = "Order.findOrdersByUserId";
    private static final Logger logger = Logger.getLogger(OrderDAOImpl.class);

    @Override
    public long makeOrder(Order order) {
        sessionFactory.getCurrentSession().saveOrUpdate(order);
        return order.getOrders_id();
    }


    @Override
    public List<Order> getOrdersByUserId(long userId) {
        return sessionFactory.getCurrentSession().getNamedQuery(GET_ORDERS_BY_USER_ID).setParameter(ID, userId).list();
    }


    @Override
    public Order getOrder(long id) {
        return (Order) sessionFactory.getCurrentSession().getNamedQuery(SELECT_ORDER_BY_ID).setParameter(ID, id).uniqueResult();

    }

}
