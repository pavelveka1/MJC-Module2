package com.epam.esm.service;

import java.util.List;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.*;

/**
 * Interface GiftCertificateService.
 * Contains methods for work with GiftCertificateDto
 */
public interface GiftCertificateService {

    /**
     * Create GiftCertificate in DB
     *
     * @param giftCertificateDto it contains data for creation giftCertificate
     * @return created GiftCertificate as GiftCertificateDto
     * @throws DuplicateEntryServiceException if this GiftCertificate already exists in the DB
     */
    GiftCertificateDto create(GiftCertificateDto giftCertificateDto) throws DuplicateEntryServiceException, TagNotExistServiceException;

    /**
     * Read GiftCertificateDto from DB by id
     *
     * @param id id of GiftCertificate
     * @return GiftCertificateDto
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    GiftCertificateDto read(long id) throws IdNotExistServiceException;


    /**
     * Update GiftCertificate as GiftCertificateDto
     *
     * @param giftCertificateDto modified GiftCertificate
     * @return updated GiftCertificateDto
     */
    GiftCertificateDto update(GiftCertificateDto giftCertificateDto) throws UpdateServiceException, IdNotExistServiceException;

    /**
     * Delete GiftCertificate from DB by id
     *
     * @param id id of GiftCertificate
     * @throws IdNotExistServiceException if record with such id not exist in DB
     */
    void delete(long id) throws IdNotExistServiceException;

    /**
     * Find all giftCertificates with condition determined by parameters
     *
     * @param sortType  name of field of table in DB
     * @param orderType ASC or DESC
     * @return list og GiftCertificates
     * @throws RequestParamServiceException if parameters don't right
     */
    List<GiftCertificateDto> findAll(String search, String value, String sortType, String orderType)
            throws RequestParamServiceException;

}
