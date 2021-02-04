package com.epam.esm.dao;

import java.util.List;

import com.epam.esm.entity.Tag;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Interface TagDAO.
 * Contains methods for work with Tag class
 */
public interface TagDAO {

    /**
     * Create new tag in DB
     *
     * @param tag will be created in DB
     * @return created Tag
     */
    long create(Tag tag);

    /**
     * Read one Tag from DB by id
     *
     * @param id Tag with this id will be read from DB
     * @return Tag
     * @throws EmptyResultDataAccessException if records with such id not exist in DB
     */
    Tag read(long id);

    /**
     * Delete Tag from DB by id
     *
     * @param id Tag with this id will be deleted from DB
     */
    int delete(long id);

    /**
     * Find all Tags
     *
     * @return list of Tags
     */
    List<Tag> findAll();

    /**
     * Get list of GiftCertificate by tag id
     *
     * @param certificateId id of gift certificate
     * @return list of tag
     */
    List<Tag> getTagsByGiftCertificateId(long certificateId);

    /**
     *  Method updates list of tags by certificate id
     * @param idCertificate id of certificate
     * @param newTags list of tags to be added
     * @param oldTags list of tags to be deleted
     */
    void updateListTagsForCertificate(long idCertificate, List<Tag> newTags, List<Tag> oldTags);

}
