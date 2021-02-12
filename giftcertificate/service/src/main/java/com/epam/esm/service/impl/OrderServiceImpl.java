package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.CertificateNameNotExistServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final int ZERO = 0;
    private static final int ONE = 1;
    /**
     * GiftSertificateJDBCTemplate is used for operations with GiftCertificate
     */
    @Autowired
    private GiftCertificateDAO giftCertificateDAO;

    /**
     * TagJDBCTemplate is used for operations with Order
     */
    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private UserDAO userDAO;

    /**
     * ModelMapper is used for convertation TagDto to Tag or GiftCertificateDto to GiftCertificate
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Create new order
     *
     * @param orderDto will be created
     * @return OrderDto
     * @throws CertificateNameNotExistServiceException if certificate is not exist in DB
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = CertificateNameNotExistServiceException.class)
    @Override
    public OrderDto makeOrder(OrderDto orderDto) throws CertificateNameNotExistServiceException {
        User user = userDAO.getUser(orderDto.getUser().getId());
        orderDto.setUser(user);
        Order order = modelMapper.map(orderDto, Order.class);
        List<GiftCertificate> giftCertificateList = orderDto.getCertificates().stream()
                .map(giftCertificateDto -> (modelMapper.map(giftCertificateDto, GiftCertificate.class))).collect(Collectors.toList());
        giftCertificateList = getFilledCertificates(giftCertificateList);
        order.setCertificates(giftCertificateList);
        int cost = calculateOrderCost(giftCertificateList);
        order.setCost(cost);
        order.setDate(LocalDateTime.now(ZoneId.systemDefault()).toString());
        orderDAO.makeOrder(order);
        return modelMapper.map(order, OrderDto.class);
    }

    /**
     * Read orders by id of user
     *
     * @param userId id of user
     * @param page   number of page
     * @param size   zise of page
     * @return List of orders
     * @throws IdNotExistServiceException if user with passed id is not exist
     * @throws PaginationException        if page equals zero
     */
    @Transactional
    @Override
    public List<OrderDto> getOrdersByUserId(long userId, Integer page, Integer size)
            throws IdNotExistServiceException, PaginationException {
        List<Order> orders;
        List<OrderDto> orderDtoList;
        checkPageAndSize(page, size);
        try {
            User user = userDAO.getUser(userId);
            orders = orderDAO.getOrdersByUserId(user, page, size);
            orderDtoList = orders.stream()
                    .map(order -> modelMapper.map(order, OrderDto.class))
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            throw new IdNotExistServiceException("User with id = " + userId + " is not exist in DB");
        }
        return orderDtoList;
    }

    /**
     * Rear order by id
     *
     * @param id of order
     * @return OrderDto
     * @throws IdNotExistServiceException if order with passed id is not exist
     */
    @Override
    public OrderDto getOrder(long id) throws IdNotExistServiceException {
        try {
            return modelMapper.map(orderDAO.getOrder(id), OrderDto.class);
        } catch (IllegalArgumentException e) {
            throw new IdNotExistServiceException("Order with id = " + id + " is not exist in DB");
        }
    }

    private int calculateOrderCost(List<GiftCertificate> giftCertificateList) throws CertificateNameNotExistServiceException {
        int cost = 0;
        for (GiftCertificate giftCertificate : giftCertificateList) {
            cost = cost + giftCertificate.getPrice();
        }
        return cost;
    }

    private List<GiftCertificate> getFilledCertificates(List<GiftCertificate> giftCertificateList)
            throws CertificateNameNotExistServiceException {
        List<GiftCertificate> certificates = new ArrayList<>();
        for (GiftCertificate giftCertificate : giftCertificateList) {
            GiftCertificate giftCertificate1 = giftCertificateDAO.readByNotDeletedName(giftCertificate.getName());
            if (giftCertificate1 == null) {
                throw new CertificateNameNotExistServiceException("Certificate with name = " + giftCertificate.getName()
                        + " is not exist in DB");
            }
            certificates.add(giftCertificate1);
        }
        return certificates;
    }

    private void checkPageAndSize(Integer page, Integer size) throws PaginationException {
        if (page < ONE) {
            if (page == ZERO) {
                throw new PaginationException("It's imposible to get page with zero number");
            }
            page = Math.abs(page);
        }
        if (size < ONE) {
            size = Math.abs(size);
        }
    }
}
