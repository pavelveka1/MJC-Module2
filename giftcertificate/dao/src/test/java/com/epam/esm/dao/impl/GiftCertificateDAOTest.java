package com.epam.esm.dao.impl;

import com.epam.esm.configuration.ApplicationConfig;
import com.epam.esm.entity.GiftCertificate;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = {GiftCertificateDAOTest.class})
public class GiftCertificateDAOTest {

    @Autowired
    @Qualifier(value = "sessionFactoryDev")
    private SessionFactory sessionFactory;

    @Autowired
    private GiftCertificateDAOImpl giftCertificateDAO;

    @BeforeAll
    public static void init() {

    }


    @DisplayName("should create gift certificate in DB and return this one")
    @Transactional
    @Test
    public void createGiftCertificates() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("New gift certificate");
        giftCertificate.setDescription("new description");
        giftCertificate.setPrice(1000);
        giftCertificate.setDuration(30);
        long id = giftCertificateDAO.create(giftCertificate);
        GiftCertificate createdGiftCertificate = giftCertificateDAO.read(id);
        assertEquals(giftCertificate.getName(), createdGiftCertificate.getName());
        assertEquals(giftCertificate.getDescription(), createdGiftCertificate.getDescription());
        assertEquals(giftCertificate.getPrice(), createdGiftCertificate.getPrice());
        assertEquals(giftCertificate.getDuration(), createdGiftCertificate.getDuration());

    }

    @DisplayName("should be thrown DuplicateKeyException ")
    @Test
    public void createGiftCertificatesDuplicateKeyException() throws ConstraintViolationException {
        GiftCertificate giftCertificate = new GiftCertificate(1, "Поeлет на дельтоплане", "description",
                1000, 30, null, null, null, null, false);
        assertThrows(ConstraintViolationException.class, () -> {
            giftCertificateDAO.create(giftCertificate);
        });
    }

    @DisplayName("should be returned not null")
    @Test
    public void createGiftCertificatesNotNull() throws ConstraintViolationException {
        GiftCertificate giftCertificate = new GiftCertificate(1, "test name 1", "description",
                1000, 30, null, null, null, null, false);
       long id = giftCertificateDAO.create(giftCertificate);
        GiftCertificate actulaGC=giftCertificateDAO.read(id);
        assertNotNull(actulaGC);
        assertEquals(giftCertificate.getName(), actulaGC.getName());
        assertEquals(giftCertificate.getDescription(), actulaGC.getDescription());
        assertEquals(giftCertificate.getPrice(), actulaGC.getPrice());
        assertEquals(giftCertificate.getDuration(), actulaGC.getDuration());
    }


    @DisplayName("read gift certificate by id ")
    @Test
    public void readGiftCertificateById() {
        GiftCertificate expected = giftCertificateDAO.read(5);
        GiftCertificate actual = new GiftCertificate();
        actual.setName("Массаж всего тела, спины или лица");
        assertEquals(expected.getName(), actual.getName());
    }

    @DisplayName("should be thrown EmptyResultDataAccessException ")
    @Test
    public void readGiftCertificateByNotExistId() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            giftCertificateDAO.read(Integer.MAX_VALUE);
        });
    }

    @DisplayName("read gift certificate by id, return not bull ")
    @Test
    public void readGiftCertificateByIdNotNull() {
        assertNotNull(giftCertificateDAO.read(5));
    }


    @DisplayName("read all gift certificates by id with sort by name")
    @Test
    public void findAllGiftCertificatesSort() throws BadSqlGrammarException {
        List<GiftCertificate> actual = giftCertificateDAO.findAll(null, null,"name",
                "id", "ASC",1, 10);
        assertEquals(1, actual.get(0).getId());
    }

    @DisplayName("should be thrown SQLSyntaxErrorException")
    @Test
    public void findAllGiftCertificatesBadSqlGrammarException() throws BadSqlGrammarException {
        assertThrows(BadSqlGrammarException.class, () -> {
            giftCertificateDAO.findAll("search",null, "notExistParam", "id",
                    "asc",1, 10);
        });
    }


    @DisplayName("should be return 1 ")
    @Test
    public void deleteGiftCertificateById() {
        GiftCertificate giftCertificate = new GiftCertificate(2, "test name 2", "description",
                1000, 30, null, null, null, null, false);
        giftCertificateDAO.delete(giftCertificate);
        assertThrows(EmptyResultDataAccessException.class, () -> {
            giftCertificateDAO.read(1);
        });
    }



    @DisplayName("should be equals name")
    @Test
    public void updateGiftCertificateExist() {
        GiftCertificate giftCertificate = new GiftCertificate(3, "test name 3", "description",
                1000, 30, null, null, null, null, false);
        giftCertificateDAO.update(giftCertificate);
        assertEquals(giftCertificate.getName(), giftCertificateDAO.read(3).getName());
    }

}
