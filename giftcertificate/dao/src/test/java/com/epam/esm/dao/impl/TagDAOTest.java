package com.epam.esm.dao.impl;

import com.epam.esm.configuration.ApplicationConfigDevProfile;
import com.epam.esm.entity.Tag;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringJUnitConfig(classes = ApplicationConfigDevProfile.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = {TagDAOTest.class})
public class TagDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TagDAOImpl tagDAOImpl;

    @DisplayName("should create tag in DB and return this one")
    @Transactional
    @Test
    public void createTag() throws SQLIntegrityConstraintViolationException {
        Tag tag = new Tag();
        tag.setName("Test tag 1");
        long id = tagDAOImpl.create(tag);
        Tag createdTag = tagDAOImpl.read(id);
        assertEquals(tag.getName(), createdTag.getName());
    }

    @DisplayName("should be thrown DuplicateKeyException")
    @Transactional
    @Test
    public void createTagDuplicateKeyException() {
        Tag tag = new Tag();
        tag.setName("Активный отдых");
        assertThrows(HibernateException.class, () -> {
            tagDAOImpl.create(tag);
        });

    }

    @DisplayName("should be return not null")
    @Transactional
    @Test
    public void createTagReturnNotNull() throws SQLIntegrityConstraintViolationException {
        Tag tag = new Tag();
        tag.setName("Test tag 2");
        long id = tagDAOImpl.create(tag);
        Tag createdTag = tagDAOImpl.read(id);
        assertNotNull(createdTag);
        assertEquals(tag.getName(), createdTag.getName());
    }


    @DisplayName("should be return null after deletion ")
    @Transactional
    @Test
    public void deleteTagByNotExistId() {
        Tag tag = new Tag();
        tag.setName("Test tag 3");
        long id = tagDAOImpl.create(tag);
        tag = tagDAOImpl.read(id);
        tagDAOImpl.delete(tag);
        assertEquals(null, tagDAOImpl.read(id));
    }


    @DisplayName("read tag by id ")
    @Transactional
    @Test
    public void readTagById() {
        Tag tag = tagDAOImpl.read(5);
        Tag actualTag = new Tag();
        actualTag.setName("Обучение");
        assertEquals(tag.getName(), actualTag.getName());
    }

    @DisplayName("should be returned null")
    @Transactional
    @Test
    public void readTagByNotExistId() {
        assertNull(tagDAOImpl.read(Integer.MAX_VALUE));
    }

    @DisplayName("read tag by id, tag is not null")
    @Transactional
    @Test
    public void readTagByIdNotNull() {
        assertNotNull(tagDAOImpl.read(5));
    }


    @DisplayName("get all tags")
    @Transactional
    @Test
    public void readAllTagsNotNull() {
        List<Tag> actual = tagDAOImpl.findAll(1, 100);
        assertEquals(15,actual.size());
    }

    @DisplayName("should be thrown IllegalArgumentException if page = zero")
    @Transactional
    @Test
    public void readAllTagsZeroPage() {
        assertThrows(IllegalArgumentException.class, () -> {
           tagDAOImpl.findAll(0, 100);
        });
    }

    @DisplayName("should be returned list with size = 2")
    @Transactional
    @Test
    public void getTagsByGiftCertificateId() {
        List<Tag> tagList=tagDAOImpl.getTagsByGiftCertificateId(1);
        assertEquals(2, tagList.size());
    }

    @DisplayName("should be returned list with size = 1 and name = Обучение")
    @Transactional
    @Test
    public void getTagsByGiftCertificateIdCheckName() {
        List<Tag> tagList=tagDAOImpl.getTagsByGiftCertificateId(6);
        assertEquals(1, tagList.size());
        assertEquals("Обучение",tagList.get(0).getName());
    }

    @DisplayName("should be returned tag with name = Авто")
    @Transactional
    @Test
    public void getTagByName() {
       Tag tag=tagDAOImpl.getTagByName("Авто");
        assertEquals("Авто", tag.getName());
    }

    @DisplayName("should be returned null")
    @Transactional
    @Test
    public void getTagByNotExistingName() {
        assertNull(tagDAOImpl.getTagByName("no name"));
    }

    @DisplayName("should be returned widely used by user tag ")
    @Transactional
    @Test
    public void getIdWidelyUsedByUserTagWithHighestCost() {
        assertEquals(1,tagDAOImpl.getIdWidelyUsedByUserTagWithHighestCost(1));
    }
}
