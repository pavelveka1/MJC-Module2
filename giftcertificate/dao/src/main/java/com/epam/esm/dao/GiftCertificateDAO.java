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
     */
    long create(GiftCertificate giftCertificate);

    /**
     * Read GiftCertificate from DB by id
     *
     * @param id long type parameter
     * @return Optional<GiftCertificate>
     */
    GiftCertificate read(long id);

    /**
     * Read GiftCertificate from DB by name
     *
     * @param certificateName name of certificate
     * @return GiftCertificate
     */
    GiftCertificate readByName(String certificateName);

    /**
     * Read only certificates which are not deleted
     * @param certificateName Name of certificate
     * @return GiftCertificate by name
     */
    GiftCertificate readByNotDeletedName(String certificateName);

    /**
     * Update GiftCertificate
     *
     * @param giftCertificate we wont update
     */
    void update(GiftCertificate giftCertificate);

    /**
     * Delete Tag from DB by id
     *
     * @param giftCertificate - certificate with id will be deleted from DB
     */
    void delete(GiftCertificate giftCertificate);

    /**
     * Find all giftCertificates with condition determined by parameters
     *
     * @param sortType  name of field of table in DB
     * @param orderType ASC or DESC
     * @return list og GiftCertificates
     */
    List<GiftCertificate> findAll(String search, List<Tag> tags, String nameOrDescription, String sortType, String orderType, Integer page, Integer size);

}
