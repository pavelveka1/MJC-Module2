package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;

public interface OrderDAO {

    long makeOrder(Order order);

    List<Order> getOrdersByUserId(User user, Integer page, Integer size);

    Order getOrder(long id);

}
