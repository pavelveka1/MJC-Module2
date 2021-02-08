package com.epam.esm.dao.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import org.apache.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Repository;

/**
 * GiftSertificateJDBCTemplate - class for work with GiftCertificate
 */
@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private static final Logger logger = Logger.getLogger(GiftCertificateDAOImpl.class);
    private static final String SELECT_GIFT_CERTIFICATES_BY_ID = "GiftCertificate.findById";
    private static final String SELECT_GIFT_CERTIFICATES_BY_NAME = "GiftCertificate.findByName";
    private static final String SELECT_ALL_CERTIFICATES_WITH_SORT = "GiftCertificate.findAll";
    private static final String SELECT_ALL_CERTIFICATES_BY_TAG_NAME = "GiftCertificate.findByTagName";
    private static final String SELECT_ALL_CERTIFICATES_BY_NAME_OR_DESCRIPTION = "GiftCertificate.findByNameOrDescription";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String NAME_OR_DESCRIPTION = "nameOrDescription";


    /**
     * Instance of SessionFactory for work with DB
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Create GiftCertificate in DB
     *
     * @param giftCertificate we wont create in DB
     * @return created GiftCertificate
     * @throws DataIntegrityViolationException if this GiftCertificate already exists in the DB
     */
    @Override
    public long create(GiftCertificate giftCertificate) throws ConstraintViolationException {
        giftCertificate.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()));
        giftCertificate.setLastUpdateDate(giftCertificate.getCreateDate());
        sessionFactory.getCurrentSession().saveOrUpdate(giftCertificate);
        return giftCertificate.getId();
    }

    /**
     * Read GiftCertificate from DB by id
     *
     * @param id long type parameter
     * @return GiftCertificate
     * @throws EmptyResultDataAccessException if records with such id not exist in DB
     */
    @Override
    public GiftCertificate read(long id) throws EmptyResultDataAccessException {
        return (GiftCertificate) sessionFactory.getCurrentSession().getNamedQuery(SELECT_GIFT_CERTIFICATES_BY_ID).setParameter(ID, id).uniqueResult();
    }

    /**
     * Read certificate by name
     *
     * @param certificateName name of certificate
     * @return GiftCertificate
     * @throws EmptyResultDataAccessException if certificate with such name is not exist
     */
    @Override
    public GiftCertificate readByName(String certificateName) throws EmptyResultDataAccessException {
        return (GiftCertificate) sessionFactory.getCurrentSession().getNamedQuery(SELECT_GIFT_CERTIFICATES_BY_NAME).setParameter(NAME, certificateName).uniqueResult();
    }

    /**
     * Update GiftCertificate
     *
     * @param giftCertificate we wont update
     */
    @Override
    public void update(GiftCertificate giftCertificate) throws NonUniqueObjectException {
        giftCertificate.setLastUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));
        sessionFactory.getCurrentSession().update(giftCertificate);
    }

    /**
     * Delete GiftCertificate from DB by id
     *
     * @param giftCertificate - GiftCertificate with id will be deleted from DB
     */
    @Override
    public void delete(GiftCertificate giftCertificate) throws ConstraintViolationException {
        sessionFactory.getCurrentSession().delete(giftCertificate);
    }

    /**
     * Find all giftCertificates with condition determined by parameters
     *
     * @param sortType  name of field of table in DB
     * @param orderType ASC or DESC
     * @return list og GiftCertificates
     * @throws BadSqlGrammarException if parameters don't right
     */
    @Override
    public List<GiftCertificate> findAll(String sortType, String orderType) throws BadSqlGrammarException {
        return sessionFactory.getCurrentSession().getNamedQuery(SELECT_ALL_CERTIFICATES_WITH_SORT).list();
    }

    /**
     * Find all giftCertificates by name of tad passed by parameters
     *
     * @param tagName   name of Tag
     * @param sortType  type of sort equals name of field in DB
     * @param orderType ASC or DESC
     * @return List of GiftCertificates
     * @throws BadSqlGrammarException if passed name not exist
     */
    @Override
    public List<GiftCertificate> findAllCertificatesByTagName(String tagName, String sortType, String orderType)
            throws BadSqlGrammarException {
        //  tagName = QUOTES + tagName + QUOTES;
        return sessionFactory.getCurrentSession().getNamedQuery(SELECT_ALL_CERTIFICATES_BY_TAG_NAME).setParameter(NAME, tagName).list();
    }

    /**
     * Find all giftCertificates by name or description of gift certificate passed by parameters
     *
     * @param nameOrDescription part of name or description
     * @param sortType          type of sort equals name of field in DB
     * @param orderType         ASC or DESC
     * @return List of GiftCertificates
     * @throws BadSqlGrammarException if passed parameter not exist
     */
    @Override
    public List<GiftCertificate> findAllCertificatesByNameOrDescription(String nameOrDescription, String sortType, String orderType)
            throws BadSqlGrammarException {
        // nameOrDescription = ANY_CHARACTERS_BEFORE + nameOrDescription + ANY_CHARACTERS_AFTER;
        return sessionFactory.getCurrentSession().getNamedQuery(SELECT_ALL_CERTIFICATES_BY_NAME_OR_DESCRIPTION).setParameter(NAME_OR_DESCRIPTION, nameOrDescription).list();
    }

}
