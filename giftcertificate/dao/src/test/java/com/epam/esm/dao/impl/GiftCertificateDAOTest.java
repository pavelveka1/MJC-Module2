package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.impl.config.ApplicationConfigDevProfile;
import com.epam.esm.entity.GiftCertificate;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(classes = ApplicationConfigDevProfile.class)
@ActiveProfiles("dev")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GiftCertificateDAOTest {

    private static final String TEST_NAME = "Поeлет на дельтоплане";
    private static final String TEST_TAG = "Спорт";
    private static GiftCertificate certificate1 = new GiftCertificate();
    private static GiftCertificate certificate2 = new GiftCertificate();
    private static GiftCertificate certificate3 = new GiftCertificate(1, "Поeлет на дельтоплане", "Полеты на мотодельтаплане " +
            "дарят кристально чистый заряд адреналина", 1000, 30, LocalDateTime.parse("2020-09-15T16:06:00"),
            LocalDateTime.parse("2020-09-15T16:06:00"), false);
    GiftCertificate certificate4 = new GiftCertificate(4, "Экстреegeмальные", "Забудьте, все",
            100, 30, LocalDateTime.parse("2020-09-15T16:06:00"), LocalDateTime.parse("2020-09-15T16:06:00"), null, null, false);

    private static Tag tag1 = new Tag(1, "Активный отдых");
    private static Tag tag2 = new Tag(2, "Спорт");
    private static List<Tag> tags = new ArrayList<>();

    @Autowired
    private GiftCertificateDAO giftCertificateDAO;

    @BeforeAll
    public static void init() {
        certificate1.setName("certificate 1");
        certificate1.setDescription("description 1");
        certificate1.setPrice(1000);
        certificate1.setDuration(30);
        certificate1.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()));
        certificate1.setLastUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));

        certificate2.setName(TEST_NAME);
        certificate2.setDescription("description 1");
        certificate2.setPrice(1000);
        certificate2.setDuration(30);
        certificate2.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()));
        certificate2.setLastUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));

        tags.add(tag1);
        tags.add(tag2);
        certificate3.setTags(tags);

    }


    @DisplayName("read gift certificate by id ")
    @Test
    public void testReadGiftCertificateById() {
        GiftCertificate actual = giftCertificateDAO.readById(new Long(1));
        assertEquals(certificate3.getName(), actual.getName());
        assertEquals(certificate3.getDescription(), actual.getDescription());
        assertEquals(certificate3.getPrice(), actual.getPrice());
        assertEquals(certificate3.getDuration(), actual.getDuration());
    }

    @DisplayName("should be returned null")
    @Test
    public void testReadGiftCertificateByNotExistId() {
        assertNull( giftCertificateDAO.readById(new Long(Integer.MAX_VALUE)));
    }


    @DisplayName("should be found one certificate")
    @Test
    public void testFindAllGiftCertificatesByDescription() throws BadSqlGrammarException {
        List<Tag> tags = new ArrayList<>();
        List<GiftCertificate> actual = giftCertificateDAO.readAll("%", "Забудьте, все", tags,
                "id", "ASC", 1, 10);
        assertEquals(1, actual.size());
    }

    @DisplayName("should be found one certificate")
    @Test
    public void testFindAllGiftCertificatesByName() throws BadSqlGrammarException {
        List<Tag> tags = new ArrayList<>();
        List<GiftCertificate> actual = giftCertificateDAO.readAll("Поeлет на дельтоплане", "%", tags,
                "id", "ASC", 1, 10);
        assertEquals(1, actual.size());
    }

    @DisplayName("should be found 3 certificates")
    @Test
    public void testFindAllGiftCertificatesByTag() throws BadSqlGrammarException {
        List<GiftCertificate> actual = giftCertificateDAO.readAll("%", "%", tags, "id",
                "ASC", 1, 10);
        assertEquals(3, actual.size());
    }


    @DisplayName("should be return equals name")
    @Test
    public void testReadByNotDeletedName() {
        assertEquals("Массаж", giftCertificateDAO.readByNotDeletedName("Массаж").getName());
    }

    @DisplayName("should not be equals null")
    @Test
    public void testReadByNotDeletedNameNotNull() {
        assertNotNull(giftCertificateDAO.readByNotDeletedName("Массаж"));
    }


    @DisplayName("should be returned null")
    @Test
    public void testReadCertificateByDeletedName() {
        assertEquals(null, giftCertificateDAO.readByNotDeletedName("Мастefeер-класс"));
    }


    @DisplayName("should be return equals name")
    @Test
    public void testReadByDeletedName() {
        GiftCertificate certificate = giftCertificateDAO.findById(new Long(5)).get();
        giftCertificateDAO.delete(certificate);
        assertEquals("Рисование", giftCertificateDAO.readByName("Рисование").getName());
    }


}
