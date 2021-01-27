package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DuplicateEntryDAOException;
import com.epam.esm.exception.IdNotExistDAOException;
import com.epam.esm.exception.RequestParamDAOException;
import com.epam.esm.exception.UpdateDAOException;

/**
 * Interface GiftCertificateDAO.
 * Contains methods for work with GiftCertificate class
 */
public interface GiftCertificateDAO {

    /**
     * Create GiftCertificate in DB
     *
     * @param giftCertificate we wont create in DB
     * @return created GiftCertificate
     * @throws DuplicateEntryDAOException if this GiftCertificate already exists in the DB
     */
    GiftCertificate create(GiftCertificate giftCertificate) throws DuplicateEntryDAOException;

    /**
     * Read GiftCertificate from DB by id
     *
     * @param id long type parameter
     * @return Optional<GiftCertificate>
     * @throws IdNotExistDAOException if records with such id not exist in DB
     */
    GiftCertificate read(long id) throws IdNotExistDAOException;

    /**
     * Update GiftCertificate
     *
     * @param giftCertificate we wont update
     * @return updated GiftCertificate
     * @throws UpdateDAOException will be thrown when giftCertificate has not been updated
     */
    GiftCertificate update(GiftCertificate giftCertificate) throws UpdateDAOException;

    /**
     * Delete Tag from DB by id
     *
     * @param id Tag with this id will be deleted from DB
     * @throws IdNotExistDAOException if records with such id not exist in DB
     */
    void delete(long id) throws IdNotExistDAOException;

    /**
     * Find all giftCertificates with condition determined by parameters
     *
     * @param sortType  name of field of table in DB
     * @param orderType ASC or DESC
     * @return list og GiftCertificates
     * @throws RequestParamDAOException if parameters don't right
     */
    List<GiftCertificate> findAll(String sortType, String orderType) throws RequestParamDAOException;

}
