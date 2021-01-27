package com.epam.esm.dao.impl;

import com.epam.esm.configuration.DBConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagMapper;
import com.epam.esm.exception.DuplicateEntryDAOException;
import com.epam.esm.exception.IdNotExistDAOException;
import com.epam.esm.exception.TagNameNotExistDAOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = DBConfig.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class TagDAOTest {

    private static final String GET_TAG_BY_ID = "SELECT * FROM gift_db.tags where gift_db.tags.id=?";
    private static final String GET_CERTIFICATES_BY_TAG_ID = "SELECT \n" +
            "gift_db.gift_certificates.id,\n" +
            "gift_db.gift_certificates.name,\n" +
            "gift_db.gift_certificates.description,\n" +
            "gift_db.gift_certificates.price,\n" +
            "gift_db.gift_certificates.duration,\n" +
            "gift_db.gift_certificates.create_date,\n" +
            "gift_db.gift_certificates.last_update_date \n" +
            "FROM gift_db.gift_certificates_has_tags\n" +
            "join gift_db.gift_certificates\n" +
            "on gift_db.gift_certificates_has_tags.gift_certificates_id = gift_db.gift_certificates.id\n" +
            "where gift_db.gift_certificates_has_tags.tags_id = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TagJDBCTemplate tagJDBCTemplate;

    @Autowired
    private GiftCertificateMapper giftCertificateMapper;

    @Autowired
    private TagMapper tagMapper;

    @DisplayName("should create tag in DB and return this one")
    @Test
    public void createTag() throws DuplicateEntryDAOException {
        Tag tag = new Tag();
        tag.setName("Test tag");

        Tag createdTag = tagJDBCTemplate.create(tag);
        long idCreatedTag = createdTag.getId();
        Tag expectedTag = jdbcTemplate.queryForObject(GET_TAG_BY_ID, new Object[]{idCreatedTag}, tagMapper);

        assertEquals(expectedTag, createdTag);
    }

    @DisplayName("should be thrown DuplicateEntryDAOException")
    @Test
    public void createTagDUplicateEntryDAOException() throws DuplicateEntryDAOException {
        Tag tag = jdbcTemplate.queryForObject(GET_TAG_BY_ID, new Object[]{1}, tagMapper);
        assertThrows(DuplicateEntryDAOException.class, () -> {
            tagJDBCTemplate.create(tag);
        });

    }

    @DisplayName("should be return not null")
    @Test
    public void createTagReturnNotNull() throws DuplicateEntryDAOException {
        Tag tag = new Tag();
        tag.setName("New Test tag");
        Tag createdTag = tagJDBCTemplate.create(tag);
        assertNotNull(createdTag);
    }

    @DisplayName("should be thrown IdNotExistDAOException ")
    @Test
    public void deleteTagById() throws IdNotExistDAOException {
        tagJDBCTemplate.delete(15);
        assertThrows(IdNotExistDAOException.class, () -> {
            tagJDBCTemplate.read(15);
        });
    }

    @DisplayName("read tag by id ")
    @Test
    public void readTagById() throws IdNotExistDAOException {
        Tag tag = tagJDBCTemplate.read(5);
        Tag actualTag = jdbcTemplate.queryForObject(GET_TAG_BY_ID, new Object[]{5}, tagMapper);
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(GET_CERTIFICATES_BY_TAG_ID, new Object[]{5}, giftCertificateMapper);
        actualTag.setCertificates(giftCertificates);
        assertEquals(tag, actualTag);
    }

    @DisplayName("should be thrown IdNotExistDAOException ")
    @Test
    public void readTagByNotExistId() throws IdNotExistDAOException {
        assertThrows(IdNotExistDAOException.class, () -> {
            tagJDBCTemplate.read(100);
        });
    }

    @DisplayName("get all tags")
    @Test
    public void readAllTagsNotNull() {
        List<Tag> tags = tagJDBCTemplate.findAll();
        assertNotNull(tags);
    }

    @DisplayName("tag is not null")
    @Test
    public void readTagByNameNotNull() throws TagNameNotExistDAOException {
        Tag tag = tagJDBCTemplate.readTagByName("Развлечения");
        assertNotNull(tag);
    }

    @DisplayName("should be thrown TagNameNotExistDAOException")
    @Test
    public void readTagByNotExistName() throws TagNameNotExistDAOException {
        assertThrows(TagNameNotExistDAOException.class, () -> {
            tagJDBCTemplate.readTagByName("NotExistName");
        });
    }

    @DisplayName("should be returned tag with name 'Развлечения'")
    @Test
    public void readTagByName() throws TagNameNotExistDAOException {
        Tag tag = tagJDBCTemplate.readTagByName("Развлечения");
        assertEquals("Развлечения", tag.getName());
    }

}
