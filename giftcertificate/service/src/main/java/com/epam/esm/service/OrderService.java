package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.CertificateNameNotExistServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;

import java.util.List;

public interface OrderService {

    OrderDto makeOrder(OrderDto orderDto) throws CertificateNameNotExistServiceException, IdNotExistServiceException;

    List<OrderDto> getOrdersByUserId(long userId, Integer page, Integer size) throws IdNotExistServiceException, PaginationException;

    OrderDto getOrder(long id);

}
