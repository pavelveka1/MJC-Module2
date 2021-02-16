package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.CertificateNameNotExistServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;

import java.util.List;

/**
 * Interface OrderService.
 * Contains methods for work with OrderDto
 */
public interface OrderService {

    /**
     * Create new Order
     *
     * @param orderDto will be created
     * @return OrderDto
     * @throws CertificateNameNotExistServiceException if pointed certificate in order is not exist
     */
    OrderDto makeOrder(OrderDto orderDto) throws CertificateNameNotExistServiceException;

    /**
     * Read orders by user id
     *
     * @param userId id of user
     * @param page   number of page
     * @param size   zise of page
     * @return List of OrderDto
     * @throws IdNotExistServiceException if user with passed id is not exist
     * @throws PaginationException        if page number equals zero
     */
    List<OrderDto> getOrdersByUserId(long userId, Integer page, Integer size) throws IdNotExistServiceException, PaginationException;

    /**
     * Read order by id
     *
     * @param id of order
     * @return OrderDto
     * @throws IdNotExistServiceException if odrer with passed id is not exist
     */
    OrderDto getOrder(long id) throws IdNotExistServiceException;

}
