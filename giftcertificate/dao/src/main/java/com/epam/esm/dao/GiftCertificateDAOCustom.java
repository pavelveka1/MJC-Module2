package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

/**
 * Castom GiftCertificateDAO
 */
public interface GiftCertificateDAOCustom {

    /**
     * * Find all giftCertificates with condition determined by parameters
     *
     * @param name        name of certificate
     * @param description description of certificate
     * @param tags        tags are attached to certificate
     * @param sortType    name of field of table in DB
     * @param orderType   ASC or DESC
     * @param page        number of page
     * @param size        size of page
     * @return list og GiftCertificates
     */
    List<GiftCertificate> readAll(String name, String description, List<Tag> tags, String sortType, String orderType,
                                  Integer page, Integer size);
}
