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
import com.epam.esm.service.exception.JwtAuthenticationException;
import com.epam.esm.service.exception.PaginationException;
import com.epam.esm.service.security.JwtUser;
import com.epam.esm.service.util.PaginationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private static final int ZERO = 0;
    private static final String ADMIN_AUTHORITY = "ROLE_ADMIN";
    private static final String FORBIDDEN = "get_order_forbidden";

    @Autowired
    private GiftCertificateDAO giftCertificateDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    @Secured("ROLE_USER")
    @Override
    public OrderDto makeOrder(OrderDto orderDto) throws CertificateNameNotExistServiceException, IdNotExistServiceException {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDAO.findByUsername(jwtUser.getUsername());
        orderDto.setUser(user);
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


    @Transactional
    @Secured("ROLE_USER")
    @Override
    public List<OrderDto> getOrdersByUserId(long userId, Integer page, Integer size)
            throws IdNotExistServiceException, PaginationException {
        Optional<User> user;
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (RoleUtil.hasAdminAuthority(jwtUser)) {
            user = userDAO.findById(userId);
        } else {
            if (userId == jwtUser.getId()) {
                user = userDAO.findById(jwtUser.getId());
            } else {
                throw new JwtAuthenticationException(FORBIDDEN);
            }
        }
        List<Order> orders;
        List<OrderDto> orderDtoList;
        int checkedPage = PaginationUtil.checkPage(page);
        int checkedSize = PaginationUtil.checkSizePage(size);
        if (!user.isPresent()) {
            throw new IdNotExistServiceException(KEY_USER_ID_NOT_EXIST);
        }
        orders = orderDAO.getOrdersByUserId(user.get().getId());
        orderDtoList = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        return orderDtoList;

    }


    @Override
    @Secured("ROLE_USER")
    public OrderDto getOrder(long id) throws IdNotExistServiceException {
        Optional<Order> order;
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        order = orderDAO.findById(id);
        if (!order.isPresent()) {
            throw new IdNotExistServiceException(KEY_ID_NOT_EXSIST);
        }
        if (!RoleUtil.hasAdminAuthority(jwtUser)) {
            if (jwtUser.getId() != order.get().getUser().getId()) {
                throw new JwtAuthenticationException(FORBIDDEN);
            }
        }
        return modelMapper.map(order.get(), OrderDto.class);
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

