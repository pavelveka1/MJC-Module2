package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

public interface GiftCertificateDAOCustom {

    List<GiftCertificate> readAll(String name, String description, List<Tag> tags, String sortType, String orderType,
                                  Integer page, Integer size);
}
