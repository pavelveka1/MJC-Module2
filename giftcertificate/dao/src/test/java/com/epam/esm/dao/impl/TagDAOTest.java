package com.epam.esm.dao.impl;

import com.epam.esm.configuration.DBConfig;
import com.epam.esm.entity.mapper.GiftCertificateMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.mapper.TagMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TagDAOImpl tagDAOImpl;

    @Autowired
    private GiftCertificateMapper giftCertificateMapper;

    @Autowired
    private TagMapper tagMapper;

    @DisplayName("should create tag in DB and return this one")
    @Test
    public void createTag() {
        Tag tag = new Tag();
        tag.setName("Test new tag");
        long id = tagDAOImpl.create(tag);
        Tag createdTag=tagDAOImpl.read(id);
        assertEquals(tag.getName(), createdTag.getName());
    }

    @DisplayName("should be thrown DuplicateKeyException")
    @Test
    public void createTagDuplicateKeyException() {
        Tag tag = new Tag();
        tag.setName("Активный отдых");
        assertThrows(DuplicateKeyException.class, () -> {
            tagDAOImpl.create(tag);
        });

    }

    @DisplayName("should be return not null")
    @Test
    public void createTagReturnNotNull() {
        Tag tag = new Tag();
        tag.setName("New Test tag");
        long id = tagDAOImpl.create(tag);
        Tag createdTag=tagDAOImpl.read(id);
        assertNotNull(createdTag);
        assertEquals(tag.getName(), createdTag.getName());
    }

    @DisplayName("should be return 0 ")
    @Test
    public void deleteTagByNotExistId() {
        int i = tagDAOImpl.delete(Integer.MAX_VALUE);
        assertEquals(0, i);
    }

    @DisplayName("should be return 1 ")
    @Test
    public void deleteTagById() {
        int i = tagDAOImpl.delete(1);
        assertEquals(1, i);
    }


    @DisplayName("read tag by id ")
    @Test
    public void readTagById() {
        Tag tag = tagDAOImpl.read(5);
        Tag actualTag = new Tag();
        actualTag.setName("Обучение");
        assertEquals(tag.getName(), actualTag.getName());
    }

    @DisplayName("should be thrown EmptyResultDataAccessException ")
    @Test
    public void readTagByNotExistId() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            tagDAOImpl.read(Integer.MAX_VALUE);
        });
    }

    @DisplayName("read tag by id, tag is not null")
    @Test
    public void readTagByIdNotNull() {
        assertNotNull(tagDAOImpl.read(5));
    }


    @DisplayName("get all tags")
    @Test
    public void readAllTagsNotNull() {
        List<Tag> actual = tagDAOImpl.findAll();
        boolean result = false;
        if (actual.size() > 13 && actual.size() < 17) {
            result = true;
        }
        assertTrue(result);
    }
}
