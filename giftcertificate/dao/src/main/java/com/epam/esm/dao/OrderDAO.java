package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Interface OrderDAO.
 * Contains methods for work with Order class
 */
public interface OrderDAO extends PagingAndSortingRepository<Order, Long> {


    /**
     * Get orders by id of user
     *
     * @param userId id of user in order
     * @return List of Orders
     */
    @Query("select distinct o from Order o inner join fetch o.user as u where " +
            "u.id = :userId ")
    List<Order> getOrdersByUserId(Long userId);

}
