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
import com.epam.esm.service.util.PaginationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String KEY_CERTIFICATE_NAME_NOT_EXIST = "certificate_name_not_exist";
    private static final String KEY_ID_NOT_EXSIST = "order_id_not_exist";
    private static final String KEY_USER_ID_NOT_EXIST = "user_id_not_exist";
    private static final String USER_ID_NOT_EXIST = "user_id_not_exist";
    private static final int ZERO = 0;
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
    @Transactional
    @Override
    public OrderDto makeOrder(OrderDto orderDto) throws CertificateNameNotExistServiceException, IdNotExistServiceException {
        Optional<User> user = userDAO.findById(orderDto.getUser().getId());
        if (!user.isPresent()) {
            throw new IdNotExistServiceException(USER_ID_NOT_EXIST);
        }
        orderDto.setUser(user.get());
        Order order = modelMapper.map(orderDto, Order.class);
        List<GiftCertificate> giftCertificateList = orderDto.getCertificates().stream()
                .map(giftCertificateDto -> (modelMapper.map(giftCertificateDto, GiftCertificate.class))).collect(Collectors.toList());
        giftCertificateList = getFilledCertificates(giftCertificateList);
        order.setCertificates(giftCertificateList);
        int cost = calculateOrderCost(giftCertificateList);
        order.setCost(cost);
        order.setDate(LocalDateTime.now(ZoneId.systemDefault()).toString());
        order = orderDAO.save(order);
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
        page = PaginationUtil.checkPage(page);
        size = PaginationUtil.checkSizePage(size);
        Optional<User> user = userDAO.findById(userId);
        if (!user.isPresent()) {
            throw new IdNotExistServiceException(KEY_USER_ID_NOT_EXIST);
        }
        orders = orderDAO.getOrdersByUserId(user.get().getId());
        orderDtoList = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
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
            return modelMapper.map(orderDAO.findById(id).get(), OrderDto.class);
        } catch (IllegalArgumentException e) {
            throw new IdNotExistServiceException(KEY_ID_NOT_EXSIST);
        }
    }

    private int calculateOrderCost(List<GiftCertificate> giftCertificateList) {
        int cost = ZERO;
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
            if (Objects.isNull(giftCertificate1)) {
                throw new CertificateNameNotExistServiceException(KEY_CERTIFICATE_NAME_NOT_EXIST, giftCertificate.getName());
            }
            certificates.add(giftCertificate1);
        }
        return certificates;
    }

}
