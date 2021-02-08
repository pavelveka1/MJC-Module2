package com.epam.esm.dao;

import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderDAO {

    long makeOrder(Order order);

    List<Order> getOrdersByUserId(long userId);

    Order getOrder(long id);

}
