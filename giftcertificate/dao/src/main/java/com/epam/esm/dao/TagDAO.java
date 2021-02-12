package com.epam.esm.dao;

import java.util.List;

import com.epam.esm.entity.Tag;

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
     */
    Tag read(long id);

    /**
     * Delete Tag from DB by id
     *
     * @param tag this will be deleted from DB
     */
    void delete(Tag tag);

    /**
     * Find all Tags
     *
     * @return list of Tags
     */
    List<Tag> findAll(Integer pageNumber, Integer pageSize);

    /**
     * Get list of GiftCertificate by tag id
     *
     * @param certificateId id of gift certificate
     * @return list of tag
     */
    List<Tag> getTagsByGiftCertificateId(long certificateId);

    /**
     * Get Tag by name
     *
     * @param tagName name of tag
     * @return Tag
     */
    Tag getTagByName(String tagName);

    /**
     * Get widely used by user tag with max sun cost
     *
     * @param userId id of user
     * @return id of widely used by user tag
     */
    Long getIdWidelyUsedByUserTagWithHighestCost(long userId);


}
