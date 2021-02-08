package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.entity.Order;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {

    /**
     * Instance of SessionFactory for work with DB
     */
    @Autowired
    private SessionFactory sessionFactory;

    private static final String ID="id";
    private static final String SELECT_ORDER_BY_ID = "Order.findById";
    private static final String GET_ORDERS_BY_USER_ID ="Order.findOrdersByUserId";

    private static final Logger logger = Logger.getLogger(OrderDAOImpl.class);
    private static final String MAKE_ORDER = "INSERT INTO orders (users_id, gift_certificates_id, cost, create_date) VALUES (?, ?, ?, ?)";



    private static final String GET_ORDERS_BY_USER_ID_AND_CERTIFICATE_NAME="select orders.id, gift_certificates.name as" +
            " certificate_name, gift_certificates.description as certificate_description, gift_certificates.duration as" +
            " certificate_duration, orders.cost, orders.create_date from orders join gift_certificates on " +
            "orders.gift_certificates_id=gift_certificates.id  where orders.users_id=? and gift_certificates.name=?";

    @Override
    public long makeOrder(Order order) {
        sessionFactory.getCurrentSession().saveOrUpdate(order);
        return order.getOrders_id();
    }


    @Override
    public List<Order> getOrdersByUserId(long userId) {
        return  sessionFactory.getCurrentSession().getNamedQuery(GET_ORDERS_BY_USER_ID).setParameter(ID, userId).list();
    }


    @Override
    public Order getOrder(long id) {
        return (Order) sessionFactory.getCurrentSession().getNamedQuery(SELECT_ORDER_BY_ID).setParameter(ID, id).uniqueResult();

    }

}
