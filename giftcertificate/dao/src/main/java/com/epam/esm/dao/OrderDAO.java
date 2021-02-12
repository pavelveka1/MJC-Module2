package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;

/**
 * Interface OrderDAO.
 * Contains methods for work with Order class
 */
public interface OrderDAO {

    /**
     * Create order
     *
     * @param order will be created
     * @return Order
     */
    long makeOrder(Order order);

    /**
     * Get orders by id of user
     *
     * @param user orders of this user will be returned
     * @param page number of page
     * @param size size of page
     * @return List of Orders
     */
    List<Order> getOrdersByUserId(User user, Integer page, Integer size);

    /**
     * Get order by id
     *
     * @param id of Order
     * @return Order
     */
    Order getOrder(long id);

}
