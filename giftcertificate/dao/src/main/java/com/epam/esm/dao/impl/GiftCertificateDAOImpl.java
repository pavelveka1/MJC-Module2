package com.epam.esm.dao.impl;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.SessionFactory;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import javax.persistence.Parameter;

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

    private static final String INSERT_CERTIFICATE = "INSERT INTO gift_certificates (name, description, price, duration, " +
            "create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String CREATE_CERTIFICATE_HAS_TAG = "INSERT INTO gift_certificates_has_tags (gift_certificates_id," +
            " tags_id) VALUES (?, ?)";
    private static final String UPDATE_GIFT_CERTIFICATE = "UPDATE gift_certificates SET name = ?, description = ?," +
            " price = ?, duration = ?, last_update_date = ? WHERE (id = ?)";
    private static final String DELETE_GIFT_CERTIFICATE = "DELETE FROM gift_certificates WHERE id = ?";
    private static final String GET_GIFT_CERTIFICATES_BY_TAG_ID = "select gc.id, gc.name, gc.description, gc.price," +
            " gc.duration, gc.create_date, gc.last_update_date from gift_certificates as gc\n" +
            "\t join gift_certificates_has_tags on gc.id=gift_certificates_has_tags.gift_certificates_id\n" +
            "     where gift_certificates_has_tags.tags_id=?";
    private static final String ANY_CHARACTERS_BEFORE = "\"%";
    private static final String ANY_CHARACTERS_AFTER = "%\"";
    private static final String QUOTES = "\"";


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
