package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.impl.config.ApplicationConfigDevProfile;
import com.epam.esm.entity.GiftCertificate;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.esm.entity.Tag;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@SpringJUnitConfig(classes = ApplicationConfigDevProfile.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = ApplicationConfigDevProfile.class)
//@DataJpaTest
@ExtendWith(SpringExtension.class)
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


    @DisplayName("should create gift certificate in DB and return this one")
    @Transactional
    @Test
    public void createGiftCertificates() {
        GiftCertificate  createdGiftCertificate  = giftCertificateDAO.save(certificate1);
        assertEquals(certificate1.getName(), createdGiftCertificate.getName());
        assertEquals(certificate1.getDescription(), createdGiftCertificate.getDescription());
        assertEquals(certificate1.getPrice(), createdGiftCertificate.getPrice());
        assertEquals(certificate1.getDuration(), createdGiftCertificate.getDuration());

    }

    @DisplayName("should be thrown ConstraintViolationException")
    @Transactional
    @Test
    public void createGiftCertificatesDuplicate() {
        assertThrows(ConstraintViolationException.class, () -> {
            giftCertificateDAO.save(certificate2);
        });

    }

    @DisplayName("read gift certificate by id ")
    @Transactional
    @Test
    public void readGiftCertificateById() {
        Optional<GiftCertificate> certificate = giftCertificateDAO.findById(new Long(1));
        GiftCertificate actual=certificate.get();
        assertEquals(certificate3.getName(), actual.getName());
        assertEquals(certificate3.getDescription(), actual.getDescription());
        assertEquals(certificate3.getPrice(), actual.getPrice());
        assertEquals(certificate3.getDuration(), actual.getDuration());
    }

    @DisplayName("should be returned null")
    @Transactional
    @Test
    public void readGiftCertificateByNotExistId() {
        assertEquals(null, giftCertificateDAO.findById(new Long(Integer.MAX_VALUE)));
    }

    @DisplayName("read gift certificate by id, return not bull ")
    @Transactional
    @Test
    public void readGiftCertificateByIdNotNull() {
        assertNotNull(giftCertificateDAO.findById(new Long(5)));
    }


    @DisplayName("should be found one certificate")
    @Transactional
    @Test
    public void findAllGiftCertificatesByName() throws BadSqlGrammarException {
        List<Tag> tags=new ArrayList<>();
        List<GiftCertificate> actual = giftCertificateDAO.readAll("name", "%", tags,
                "id", "ASC", 1, 10);
        assertEquals(1, actual.size());
    }

    @DisplayName("should be found 3 certificates")
    @Transactional
    @Test
    public void findAllGiftCertificatesByTag() throws BadSqlGrammarException {
        List<GiftCertificate> actual = giftCertificateDAO.readAll("%", "%", tags, "id",
                "ASC", 1,  10);
        assertEquals(3, actual.size());
    }


    @DisplayName("should be return null")
    @Transactional
    @Test
    public void deleteGiftCertificateById() {
        assertEquals("Экстрeмальные", giftCertificateDAO.findById(new Long(4)).get().getName());
        giftCertificateDAO.deleteById(new Long(4));
        assertEquals(null, giftCertificateDAO.findById(new Long(4)));
    }

    @DisplayName("should be return equals name")
    @Transactional
    @Test
    public void readByNotDeletedName() {
        assertEquals("Массаж", giftCertificateDAO.readByNotDeletedName("Массаж").getName());
    }

    @DisplayName("should not be equals null")
    @Transactional
    @Test
    public void readByNotDeletedNameNotNull() {
        assertNotNull(giftCertificateDAO.readByNotDeletedName("Массаж"));
    }

    @DisplayName("should be return equals name")
    @Transactional
    @Test
    public void readByDeletedName() {
        GiftCertificate certificate = giftCertificateDAO.findById(new Long(5)).get();
        giftCertificateDAO.delete(certificate);
        assertEquals("Рисование", giftCertificateDAO.readByName("Рисование").getName());
    }


}
