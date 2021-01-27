package com.epam.esm.dao.impl;

import com.epam.esm.configuration.DBConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagMapper;
import com.epam.esm.exception.DuplicateEntryDAOException;
import com.epam.esm.exception.IdNotExistDAOException;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.esm.exception.RequestParamDAOException;
import com.epam.esm.exception.UpdateDAOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

@SpringJUnitConfig(classes = DBConfig.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class GiftCertificateDAOTest {

    private static final String SELECT_ALL_CERTIFICATES = "SELECT * FROM gift_db.gift_certificates;";
    private static final String SELECT_ALL_CERTIFICATES_SORT_BY_NAME = "SELECT * FROM gift_db.gift_certificates order by name DESC;";
    private static final String GET_GIFT_CERTIFICATE_BY_ID = "SELECT * FROM gift_db.gift_certificates where gift_certificates.id=?;";
    private static final String UPDATE_GIFT_CERTIFICATE = "UPDATE `gift_db`.`gift_certificates` SET `name` = ?, `description` = ?, `price` = ?, `duration` = ?, `last_update_date` = ? WHERE (`id` = ?);\n";
    private static final String SELECT_TAGS_BY_CERTIFICATE_ID = "SELECT id, name FROM gift_db.gift_certificates_has_tags\n" +
            "join gift_db.tags\n" +
            "on gift_db.gift_certificates_has_tags.tags_id = gift_db.tags.id\n" +
            "where gift_db.gift_certificates_has_tags.gift_certificates_id=?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GiftCertificateJDBCTemplate giftSertificateJDBCTemplate;

    @Autowired
    private GiftCertificateMapper giftCertificateMapper;

    @Autowired
    private TagMapper tagMapper;

    @BeforeAll
    public static void init() {

    }

    @DisplayName("should create gift certificate in DB and return this one")
    @Test
    public void createGiftCertificates() throws DuplicateEntryDAOException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("Test 2 gift certificate");
        giftCertificate.setDescription("Description of test 2 gift certificate");
        giftCertificate.setPrice(10);
        giftCertificate.setDuration(30);

        GiftCertificate createdGiftCertificate = giftSertificateJDBCTemplate.create(giftCertificate);
        long idCreatedGiftCertificate = createdGiftCertificate.getId();
        GiftCertificate expectedGiftCertificate = jdbcTemplate.queryForObject(GET_GIFT_CERTIFICATE_BY_ID, new Object[]{idCreatedGiftCertificate}, giftCertificateMapper);
        assertEquals(expectedGiftCertificate, createdGiftCertificate);
    }

    @DisplayName("return value shouldn't be null")
    @Test
    public void createGiftCertificatesNotNull() throws DuplicateEntryDAOException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("Test gift certificate");
        giftCertificate.setDescription("Description of test gift certificate");
        giftCertificate.setPrice(10);
        giftCertificate.setDuration(30);

        GiftCertificate createdGiftCertificate = giftSertificateJDBCTemplate.create(giftCertificate);
        assertNotNull(createdGiftCertificate);
    }

    @DisplayName("method should throw DuplicateEntryDAOException")
    @Test
    public void createGiftCertificatesDuplicateEntryDAOException() throws DuplicateEntryDAOException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("Поeлет на дельтоплане");
        giftCertificate.setDescription("it doesn't matter");
        giftCertificate.setPrice(10);
        giftCertificate.setDuration(30);

        assertThrows(DuplicateEntryDAOException.class, () -> {
            giftSertificateJDBCTemplate.create(giftCertificate);
        });
    }


    @DisplayName("method should update name of gift certificate")
    @Test
    public void updateGiftCertificateName() throws UpdateDAOException {
        GiftCertificate giftCertificate = jdbcTemplate.queryForObject(GET_GIFT_CERTIFICATE_BY_ID, new Object[]{3}, giftCertificateMapper);
        giftCertificate.setName("name for update");
        giftSertificateJDBCTemplate.update(giftCertificate);
        GiftCertificate actualGiftCertificate = jdbcTemplate.queryForObject(GET_GIFT_CERTIFICATE_BY_ID, new Object[]{3}, giftCertificateMapper);
        assertEquals(giftCertificate.getName(), actualGiftCertificate.getName());
    }

    @DisplayName("return value shouldn't be null")
    @Test
    public void updateGiftCertificateReturnNotNull() throws UpdateDAOException {
        GiftCertificate giftCertificate = jdbcTemplate.queryForObject(GET_GIFT_CERTIFICATE_BY_ID, new Object[]{2}, giftCertificateMapper);
        giftCertificate.setName("new name for update");
        assertNotNull(giftSertificateJDBCTemplate.update(giftCertificate));
    }

    @DisplayName("return value shouldn't be null")
    @Test
    public void readGiftCertificateById() throws IdNotExistDAOException {
        GiftCertificate giftCertificate = giftSertificateJDBCTemplate.read(1);
        GiftCertificate expected = jdbcTemplate.queryForObject(GET_GIFT_CERTIFICATE_BY_ID, new Object[]{1}, giftCertificateMapper);
        List<Tag> tags = jdbcTemplate.query(SELECT_TAGS_BY_CERTIFICATE_ID, new Object[]{expected.getId()}, tagMapper);
        expected.setTags(tags);
        assertEquals(giftCertificate, expected);
    }

    @DisplayName("return value shouldn't be null")
    @Test
    public void readGiftCertificateByIdNotNull() throws IdNotExistDAOException {
        GiftCertificate giftCertificate = giftSertificateJDBCTemplate.read(1);

        assertNotNull(giftCertificate);
    }

    @DisplayName("throw IdNotExistDAOException")
    @Test
    public void readGiftCertificateByNotExistId() throws IdNotExistDAOException {
        assertThrows(IdNotExistDAOException.class, () -> {
            giftSertificateJDBCTemplate.read(100);
        });
    }


    @DisplayName("should return all gift certificates")
    @Test
    public void findAllGiftCertificatesNotNull() throws RequestParamDAOException {
        List<GiftCertificate> result = giftSertificateJDBCTemplate.findAll(null, null);
        assertNotNull(result);
    }

    @DisplayName("should return all gift certificates")
    @Test
    public void findAllGiftCertificatesSortAndOrder() throws RequestParamDAOException {
        List<GiftCertificate> actual = giftSertificateJDBCTemplate.findAll("name", "DESC");
        List<GiftCertificate> expect = jdbcTemplate.query(SELECT_ALL_CERTIFICATES_SORT_BY_NAME, giftCertificateMapper);
        GiftCertificate actualGiftCertificate;
        GiftCertificate expectedGiftCertificate;
        for (int i = 0; i < actual.size(); i++) {
            actualGiftCertificate = actual.get(i);
            expectedGiftCertificate = expect.get(i);
            assertEquals(actualGiftCertificate.getId(), expectedGiftCertificate.getId());
        }
    }

    @DisplayName("should be thrown RequestParamDAOException")
    @Test
    public void findAllGiftCertificatesBadRequestParams() {
        assertThrows(RequestParamDAOException.class, () -> {
            giftSertificateJDBCTemplate.findAll("not_exist_field", "DESC");
        });
    }

    @DisplayName("gift certificate should be deleted in db")
    @Test
    public void deleteGiftCertificatesById() throws IdNotExistDAOException {
        giftSertificateJDBCTemplate.delete(1);
        assertThrows(IdNotExistDAOException.class, () -> {
            giftSertificateJDBCTemplate.read(1);
        });
    }
}
