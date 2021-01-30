package com.epam.esm.dao.impl;

import com.epam.esm.configuration.DBConfig;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.mapper.GiftCertificateMapper;
import com.epam.esm.entity.mapper.TagMapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

@SpringJUnitConfig(classes = DBConfig.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class GiftCertificateDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GiftCertificateDAO giftCertificateDAO;

    @Autowired
    private GiftCertificateMapper giftCertificateMapper;

    @Autowired
    private TagMapper tagMapper;

    @BeforeAll
    public static void init() {

    }

    @DisplayName("should create gift certificate in DB and return this one")
    @Test
    public void createGiftCertificates() throws SQLIntegrityConstraintViolationException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("Test 2 gift certificate");
        giftCertificate.setDescription("Description of test 2 gift certificate");
        giftCertificate.setPrice(10);
        giftCertificate.setDuration(30);
        GiftCertificate createdGiftCertificate = giftCertificateDAO.create(giftCertificate);
        assertEquals(giftCertificate.getName(), createdGiftCertificate.getName());
        assertEquals(giftCertificate.getDescription(), createdGiftCertificate.getDescription());
        assertEquals(giftCertificate.getPrice(), createdGiftCertificate.getPrice());
        assertEquals(giftCertificate.getDuration(), createdGiftCertificate.getDuration());
    }

    @DisplayName("should be thrown DuplicateKeyException ")
    @Test
    public void createGiftCertificatesDuplicateKeyException() throws DuplicateKeyException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("Поeлет на дельтоплане");
        giftCertificate.setPrice(1000);
        giftCertificate.setDuration(30);
        giftCertificate.setDescription("Полеты на мотодельтаплане дарят кристально чистый заряд адреналина");
        assertThrows(DuplicateKeyException.class, () -> {
            giftCertificateDAO.create(giftCertificate);
        });
    }

    @DisplayName("should be returned not null")
    @Test
    public void createGiftCertificatesNotNull() throws DuplicateKeyException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("Test 3 gift certificate");
        giftCertificate.setDescription("Description of test 3 gift certificate");
        giftCertificate.setPrice(10);
        giftCertificate.setDuration(30);
        GiftCertificate actulaGC = giftCertificateDAO.create(giftCertificate);
        assertNotNull(actulaGC);
        assertEquals(giftCertificate.getName(), actulaGC.getName());
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
    public void findAllGiftCertificatesSort() throws SQLSyntaxErrorException {
        List<GiftCertificate> actual = giftCertificateDAO.findAll("id", "ASC");
        assertEquals(1, actual.get(0).getId());
    }

    @DisplayName("should be thrown SQLSyntaxErrorException")
    @Test
    public void findAllGiftCertificatesSQLSyntaxErrorException() throws BadSqlGrammarException {
        assertThrows(BadSqlGrammarException.class, () -> {
            giftCertificateDAO.findAll("notExistParam", "DESC");
        });
    }

    @DisplayName("should be thrown SQLSyntaxErrorException")
    @Test
    public void findAllGiftCertificatesByTagName() throws BadSqlGrammarException {
        assertThrows(BadSqlGrammarException.class, () -> {
            giftCertificateDAO.findAllCertificatesByTagName("спорт", "notExistParam", "DESC");
        });
    }

    @DisplayName("should be thrown SQLSyntaxErrorException")
    @Test
    public void findAllGiftCertificatesByNameOrDescription() throws BadSqlGrammarException {
        assertThrows(BadSqlGrammarException.class, () -> {
            giftCertificateDAO.findAllCertificatesByNameOrDescription("спорт", "notExistParam", "DESC");
        });
    }

    @DisplayName("should be return 1 ")
    @Test
    public void deleteGiftCertificateById() {
        int i = giftCertificateDAO.delete(10);
        assertEquals(1, i);
    }

    @DisplayName("should be returned 0")
    @Test
    public void deleteGiftCertificateByNotExistId() {
        int i = giftCertificateDAO.delete(Integer.MAX_VALUE);
        assertEquals(0, i);
    }

    @DisplayName("should be returned 1")
    @Test
    public void updateGiftCertificateExist() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        giftCertificate.setName("Поeлет на дельтоплане");
        giftCertificate.setPrice(1000);
        giftCertificate.setDuration(30);
        giftCertificate.setDescription("Полеты на мотодельтаплане дарят кристально чистый заряд адреналина");
        giftCertificate.setName("new name");
        int i = giftCertificateDAO.update(giftCertificate);
        assertEquals(1, i);
    }

    @DisplayName("should be returned 0")
    @Test
    public void updateGiftCertificateNotExist() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("new name");
        giftCertificate.setDescription("new description");
        int i = giftCertificateDAO.update(giftCertificate);
        assertEquals(0, i);
    }
}
