package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.impl.config.ApplicationConfigDevProfile;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringJUnitConfig(classes = ApplicationConfigDevProfile.class)
@ActiveProfiles("dev")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TagDAOTest {

    @Autowired
    private TagDAO tagDAO;

    @DisplayName("should create tag in DB and return this one")
    @Test
    public void createTag() throws SQLIntegrityConstraintViolationException {
        Tag tag = new Tag();
        tag.setName("Test tag 1");
        Tag tag1 = tagDAO.save(tag);
        Optional<Tag> createdTag = tagDAO.findById(tag1.getId());
        assertEquals(tag.getName(), createdTag.get().getName());
    }

    @DisplayName("should be returned list with size = 2")
    @Test
    public void getTagsByGiftCertificateId() {
        List<Tag> tagList = tagDAO.getTagsByGiftCertificateId(1);
        assertEquals(2, tagList.size());
    }

    @DisplayName("should be returned list with size = 1 and name = Обучение")
    @Test
    public void getTagsByGiftCertificateIdCheckName() {
        List<Tag> tagList = tagDAO.getTagsByGiftCertificateId(6);
        assertEquals(1, tagList.size());
        assertEquals("Обучение", tagList.get(0).getName());
    }

    @DisplayName("should be returned widely used by user tag ")
    @Test
    public void getIdWidelyUsedByUserTagWithHighestCost() {
        assertEquals(1, tagDAO.getIdWidelyUsedByUserTagWithHighestCost(1));
    }

    @DisplayName("result should be true")
    @Test
    public void getIdWidelyUsedByUserTagWithHighestCostNotEquals() {
        assertNotEquals(2, tagDAO.getIdWidelyUsedByUserTagWithHighestCost(1));
    }

}
