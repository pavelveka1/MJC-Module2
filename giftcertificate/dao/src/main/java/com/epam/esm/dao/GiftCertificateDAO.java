package com.epam.esm.dao;

import java.util.List;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;

/**
 * Interface GiftCertificateDAO.
 * Contains methods for work with GiftCertificate class
 */
public interface GiftCertificateDAO {

    /**
     * Create GiftCertificate in DB
     *
     * @param giftCertificate we wont create in DB
     * @return id of created gift certificate
     * @throws DataIntegrityViolationException if this GiftCertificate already exists in the DB
     */
    long create(GiftCertificate giftCertificate);

    /**
     * Read GiftCertificate from DB by id
     *
     * @param id long type parameter
     * @return Optional<GiftCertificate>
     * @throws EmptyResultDataAccessException if records with such id not exist in DB
     */
    GiftCertificate read(long id);

    /**
     * Update GiftCertificate
     *
     * @param giftCertificate we wont update
     * @return updated GiftCertificate
     */
    int update(GiftCertificate giftCertificate);

    /**
     * Delete Tag from DB by id
     *
     * @param id Tag with this id will be deleted from DB
     */
    int delete(long id);

    /**
     * Find all giftCertificates with condition determined by parameters
     *
     * @param sortType  name of field of table in DB
     * @param orderType ASC or DESC
     * @return list og GiftCertificates
     * @throws BadSqlGrammarException if parameters don't right
     */
    List<GiftCertificate> findAll(String sortType, String orderType);

    /**
     * Method finds all certificates where name of tag equals tagName
     *
     * @param tagName   name of Tag
     * @param sortType  type of sort equals name of field in DB
     * @param orderType ASC or DESC
     * @return List of GiftCertificate
     * @throws BadSqlGrammarException if passed not correct parameters
     */
    List<GiftCertificate> findAllCertificatesByTagName(String tagName, String sortType, String orderType);

    /**
     * Method finds all certificates where name of name or description correlate with nameOrDescription
     *
     * @param nameOrDescription part of name or description
     * @param sortType          type of sort equals name of field in DB
     * @param orderType         ASC or DESC
     * @return List of GiftCertificate
     * @throws BadSqlGrammarException if passed not correct parameters
     */
    List<GiftCertificate> findAllCertificatesByNameOrDescription(String nameOrDescription, String sortType, String orderType);

    /**
     * Method attach tags to gift certificate
     *
     * @param idGiftCertificate id of gift certificate
     * @param tags              list of tags
     * @throws DataIntegrityViolationException exception
     */
    void attachTags(long idGiftCertificate, List<Tag> tags);

}
