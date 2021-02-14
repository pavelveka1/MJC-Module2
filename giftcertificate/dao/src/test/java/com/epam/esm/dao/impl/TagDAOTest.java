package com.epam.esm.dao.impl;

import com.epam.esm.configuration.ApplicationConfig;
import com.epam.esm.entity.Tag;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringJUnitConfig(classes = ApplicationConfig.class)
@SpringBootTest
@ActiveProfiles("dev")
public class TagDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TagDAOImpl tagDAOImpl;

    @DisplayName("should create tag in DB and return this one")
    @Test
    public void createTag() throws SQLIntegrityConstraintViolationException {
        Tag tag = new Tag();
        tag.setName("Test new tag");
        long id = tagDAOImpl.create(tag);
        Tag createdTag = tagDAOImpl.read(id);
        assertEquals(tag.getName(), createdTag.getName());
    }

    @DisplayName("should be thrown DuplicateKeyException")
    @Test
    public void createTagDuplicateKeyException() {
        Tag tag = new Tag();
        tag.setName("Активный отдых");
        assertThrows(HibernateException.class, () -> {
            tagDAOImpl.create(tag);
        });

    }

    @DisplayName("should be return not null")
    @Test
    public void createTagReturnNotNull() throws SQLIntegrityConstraintViolationException {
        Tag tag = new Tag();
        tag.setName("New Test tag");
        long id = tagDAOImpl.create(tag);
        Tag createdTag = tagDAOImpl.read(id);
        assertNotNull(createdTag);
        assertEquals(tag.getName(), createdTag.getName());
    }


    @DisplayName("should be return null after deletion ")
    @Test
    public void deleteTagByNotExistId() {
        Tag tag = new Tag();
        tag.setId(2);
        tag.setName("Test tag 2");
        tagDAOImpl.delete(tag);
        assertEquals(null, tagDAOImpl.read(2));
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
        assertThrows(HibernateException.class, () -> {
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
        List<Tag> actual = tagDAOImpl.findAll(1,10);
        boolean result = false;
        if (actual.size() > 13 && actual.size() < 17) {
            result = true;
        }
        assertTrue(result);
    }

}
