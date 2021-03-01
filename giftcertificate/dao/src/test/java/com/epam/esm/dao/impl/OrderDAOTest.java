package com.epam.esm.dao.impl;

import com.epam.esm.dao.impl.config.ApplicationConfigDevProfile;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = ApplicationConfigDevProfile.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = {UserDAOTest.class})
public class OrderDAOTest {

    private static GiftCertificate certificate1 = new GiftCertificate(1, "Поeлет на дельтоплане", "Полеты на мотодельтаплане " +
            "дарят кристально чистый заряд адреналина", 1000, 30, LocalDateTime.parse("2020-10-15T16:06:00"),
            LocalDateTime.parse("2020-10-15T16:06:00"), false);
    private static List<Tag> emptyTagList = new ArrayList<>();
    private static GiftCertificate certificate2 = new GiftCertificate(4, "Экстрeмальные", "Забудьте, все",
            100, 30, LocalDateTime.parse("2020-10-15T16:06:00"), LocalDateTime.parse("2020-10-15T16:06:00"), null, emptyTagList, false);
    private static Tag tag1 = new Tag(1, "Активный отдых");
    private static Tag tag2 = new Tag(2, "Спорт");
    private static List<GiftCertificate> certificates = new ArrayList<>();
    private static List<Tag> tags = new ArrayList<>();
    private static User user = new User(1, "Adeline", "Donal", "firstName1", "lastName1");
    private static Order order1 = new Order(1, user, 100, "2021-02-04 17:00:00", certificates);
    private static Order order2 = new Order(2, user, 100, "2021-02-04 17:00:00", certificates);
    private static Order order3 = new Order(3, user, 1000, "2021-02-04 17:00:00", certificates);
    private static List<Order> orders = new ArrayList<>();

    @Autowired
    private OrderDAO orderDAO;


    @BeforeAll
    public static void init() {
        tags.add(tag1);
        tags.add(tag2);
        certificate1.setTags(tags);
        certificates.add(certificate1);
        certificates.add(certificate2);
        order1.setCertificates(certificates);
        orders.add(order1);
        orders.add(order2);
    }

    /*
    @DisplayName("read order by id ")
    @Transactional
    @Test
    public void readOrderById() {
        assertEquals(order1, orderDAO.getOrder(1));
    }

    @DisplayName("read order by not existing id ")
    @Transactional
    @Test
    public void readOrderByNotExistId() {
        assertEquals(null, orderDAO.getOrder(100));
    }

    @DisplayName("read order by id ")
    @Transactional
    @Test
    public void readOrdersByUserId() {
        assertEquals(orders, orderDAO.getOrdersByUserId(user, 1, 5));
    }

    @DisplayName(" should be thrown IllegalArgumentException if page = zero ")
    @Transactional
    @Test
    public void readOrdersByUserIdZeroPage() {
        assertThrows(IllegalArgumentException.class, () -> {
            orderDAO.getOrdersByUserId(user, 0, 5);
        });
    }

    @DisplayName("make order")
    @Transactional
    @Test
    public void makeOrder() {
        long id = orderDAO.makeOrder(order3);
        assertEquals(order3, orderDAO.getOrder(id));
    }


     */
}
