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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

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


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = CertificateNameNotExistServiceException.class)
    @Override
    public OrderDto makeOrder(OrderDto orderDto) throws CertificateNameNotExistServiceException {
        User user = userDAO.getUser(orderDto.getUser().getId());
        orderDto.setUser(user);
        Order order = modelMapper.map(orderDto, Order.class);
        List<GiftCertificate> giftCertificateList = orderDto.getCertificates();
        int cost = calculateOrderCost(giftCertificateList);
        order.setCost(cost);
        giftCertificateList = getFilledCertificates(giftCertificateList);
        order.setCertificates(giftCertificateList);
        order.setDate(LocalDateTime.now(ZoneId.systemDefault()).toString());
        long idOrder = orderDAO.makeOrder(order);
        return modelMapper.map(order, OrderDto.class);
    }

    @Transactional
    @Override
    public List<OrderDto> getOrdersByUserId(long userId) throws IdNotExistServiceException {
        List<Order> orders;
        List<OrderDto> orderDtoList;
        try {
            orders = orderDAO.getOrdersByUserId(userId);
            orderDtoList = orders.stream()
                    .map(order -> modelMapper.map(order, OrderDto.class))
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            throw new IdNotExistServiceException("User with id = " + userId + " is not exist in DB");
        }
        return orderDtoList;
    }

    @Override
    public OrderDto getOrder(long id) {
        return modelMapper.map(orderDAO.getOrder(id), OrderDto.class);
    }

    private int calculateOrderCost(List<GiftCertificate> giftCertificateList) throws CertificateNameNotExistServiceException {
        int cost = 0;
        for (GiftCertificate giftCertificate : giftCertificateList) {
            GiftCertificate giftCertificate1 = giftCertificateDAO.readByName(giftCertificate.getName());
            if (giftCertificate1 == null) {
                throw new CertificateNameNotExistServiceException("Certificate with name = " + giftCertificate.getName() + " is not exist in DB");
            }
            cost = cost + giftCertificate1.getPrice();
        }
        return cost;
    }

    private List<GiftCertificate> getFilledCertificates(List<GiftCertificate> giftCertificateList) throws CertificateNameNotExistServiceException {
        List<GiftCertificate> certificates = new ArrayList<>();
        for (GiftCertificate giftCertificate : giftCertificateList) {
            GiftCertificate giftCertificate1 = giftCertificateDAO.readByName(giftCertificate.getName());
            if (giftCertificate1 == null) {
                throw new CertificateNameNotExistServiceException("Certificate with name = " + giftCertificate.getName() + " is not exist in DB");
            }
            certificates.add(giftCertificate1);
        }
        return certificates;
    }
}
