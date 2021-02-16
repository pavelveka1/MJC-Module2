package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.dao.impl.OrderDAOImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.CertificateNameNotExistServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class OrderServiceImplTest {

    private static User user1 = new User(1, "firstName1", "lastName1", null);
    private static User user2 = new User(2, "firstName2", "lastName2", null);
    private static GiftCertificate giftCertificate = new GiftCertificate(1, "Test name", "Test description",
            10, 20, null, null, null, null, false);
    private static GiftCertificate giftCertificate2 = new GiftCertificate(2, "Test name 2", "Test description 2",
            10, 20, null, null, null, null, false);
    private static GiftCertificateDto giftCertificateDto = new GiftCertificateDto(1, "Test name", "Test description",
            10, 20, null, null, new ArrayList<>());
    private static GiftCertificateDto giftCertificateDto2 = new GiftCertificateDto(2, "Test name 2",
            "Test description 2", 10, 20, null, null, null);
    private static List<GiftCertificate> giftCertificateList = new ArrayList<>();
    private static List<GiftCertificate> giftCertificateList2 = new ArrayList<>();
    private static List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
    private static List<GiftCertificateDto> giftCertificateDtoList2 = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static List<OrderDto> orderDtoList = new ArrayList<>();
    private static Order order1 = new Order(1, user1, 1, null, giftCertificateList);
    private static Order order2 = new Order(2, user2, 2, null, giftCertificateList2);
    private static OrderDto orderDto1 = new OrderDto(2, user1, 2, null, giftCertificateDtoList);
    private static OrderDto orderDto2 = new OrderDto(2, user2, 2, null, giftCertificateDtoList2);

    @BeforeAll
    public static void init() {
        orders.add(order1);
        orders.add(order2);
        orderDtoList.add(orderDto1);
        orderDtoList.add(orderDto2);
        users.add(user1);
        users.add(user2);
        giftCertificateDtoList.add(giftCertificateDto);
        giftCertificateDtoList.add(giftCertificateDto2);
    }

    @Mock
    private OrderDAOImpl orderDAOImpl;

    @Mock
    private UserDAO userDAO;

    @Mock
    private GiftCertificateDAO giftCertificateDAO;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService = new OrderServiceImpl();

    @DisplayName("should be returned OrderDto")
    @Test
    public void makeOrder() throws CertificateNameNotExistServiceException {
        when(userDAO.getUser(2)).thenReturn(user2);
        when(modelMapper.map(orderDto2, Order.class)).thenReturn(order2);
        when(modelMapper.map(order2, OrderDto.class)).thenReturn(orderDto2);
        when(orderDAOImpl.makeOrder(order2)).thenReturn((long) 2);
        assertEquals(orderDto2, orderService.makeOrder(orderDto2));
    }

    @DisplayName("should be thrown CertificateNameNotExistServiceException")
    @Test
    public void makeOrderCertificateNameNotExist() throws CertificateNameNotExistServiceException {
        when(userDAO.getUser(1)).thenReturn(user1);
        when(giftCertificateDAO.readByNotDeletedName("Test name")).thenReturn(null);
        when(modelMapper.map(orderDto1, Order.class)).thenReturn(order1);
        when(modelMapper.map(giftCertificateDto, GiftCertificate.class)).thenReturn(giftCertificate);
        assertThrows(CertificateNameNotExistServiceException.class, () -> {
            orderService.makeOrder(orderDto1);
        });
    }


    @DisplayName("should be returned list OrderDto")
    @Test
    public void getOrdersByUserId() throws IdNotExistServiceException, PaginationException {
        when(userDAO.getUser(1)).thenReturn(user1);
        when(modelMapper.map(order2, OrderDto.class)).thenReturn(orderDto2);
        when(modelMapper.map(order1, OrderDto.class)).thenReturn(orderDto1);
        when(orderDAOImpl.getOrdersByUserId(user1, 1, 10)).thenReturn(orders);
        assertEquals(orderDtoList, orderService.getOrdersByUserId(1, 1, 10));
    }

    @DisplayName("should be thrown PaginationException")
    @Test
    public void getOrdersByUserIdZeroPage() throws IdNotExistServiceException, PaginationException {
        assertThrows(PaginationException.class, () -> {
            orderService.getOrdersByUserId(1, 0, 10);
        });
    }

    @DisplayName("should be returned OrderDto")
    @Test
    public void getOrderById() throws IdNotExistServiceException {
        when(modelMapper.map(order1, OrderDto.class)).thenReturn(orderDto1);
        when(orderDAOImpl.getOrder(1)).thenReturn(order1);
        assertEquals(orderDto1, orderService.getOrder(1));
    }

    @DisplayName("should be thrown IdNotExistServiceException")
    @Test
    public void getOrdersByNotExistId() throws IdNotExistServiceException {
        when(modelMapper.map(null, OrderDto.class)).thenThrow(IllegalArgumentException.class);
        when(orderDAOImpl.getOrder(1)).thenReturn(null);
        assertThrows(IdNotExistServiceException.class, () -> {
            orderService.getOrder(1);
        });
    }

}
