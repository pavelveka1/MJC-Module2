package com.epam.esm.dao.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;

/**
 * GiftSertificateJDBCTemplate - class for work with GiftCertificate
 */
@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private static final Logger logger = Logger.getLogger(GiftCertificateDAOImpl.class);
    private static final String SELECT_GIFT_CERTIFICATES_BY_ID = "GiftCertificate.findById";
    private static final String SELECT_GIFT_CERTIFICATES_BY_NAME = "GiftCertificate.findByName";
    private static final String SELECT_ALL_CERTIFICATES_BY_TAG_NAME = "select gc.id, gc.name, gc.description, gc.price, " +
            "gc.duration, gc.create_date, gc.last_update_date from gift_certificates as gc\n" +
            "\t join gift_certificates_has_tags on gc.id=gift_certificates_has_tags.gift_certificates_id\n" +
            "     join tags on tags.tag_id=gift_certificates_has_tags.tags_id\n" +
            "     where tags.name=%s order by %s %s";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAG = "tag";
    private static final String ANY_CHARACTERS = "%";
    private static final String QUOTES = "\"";
    private static final String ORDER_TYPE_DEFAULT = "asc";
    private static final String TAGS = "tags";
    private static final int ONE=1;


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
        getSession().merge(giftCertificate);
        getSession().saveOrUpdate(giftCertificate);
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
        return (GiftCertificate) getSession().getNamedQuery(SELECT_GIFT_CERTIFICATES_BY_ID).setParameter(ID, id).uniqueResult();
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
        return (GiftCertificate) getSession().getNamedQuery(SELECT_GIFT_CERTIFICATES_BY_NAME).setParameter(NAME, certificateName).uniqueResult();
    }

    /**
     * Update GiftCertificate
     *
     * @param giftCertificate we wont update
     */
    @Override
    public void update(GiftCertificate giftCertificate) throws NonUniqueObjectException {
        giftCertificate.setLastUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));
        getSession().save(giftCertificate);
    }

    /**
     * Delete GiftCertificate from DB by id
     *
     * @param giftCertificate - GiftCertificate with id will be deleted from DB
     */
    @Override
    public void delete(GiftCertificate giftCertificate) throws ConstraintViolationException {
        getSession().delete(giftCertificate);
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
    public List<GiftCertificate> findAll(String search, List<Tag> tags, String nameOrDescription, String sortType, String orderType, Integer page, Integer size) throws BadSqlGrammarException {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cr = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = cr.from(GiftCertificate.class);
        if (search.equals(NAME)) {
            cr.select(giftCertificateRoot).where(cb.like(giftCertificateRoot.get(NAME), ANY_CHARACTERS + nameOrDescription + ANY_CHARACTERS));
        } else if (search.equals(DESCRIPTION)) {
            cr.select(giftCertificateRoot).where(cb.like(giftCertificateRoot.get(DESCRIPTION), ANY_CHARACTERS + nameOrDescription + ANY_CHARACTERS));
        } else if (search.equals(TAG)) {
            Predicate[] predicates = getPredicatesForTags(tags, cb, giftCertificateRoot);
            cr.select(giftCertificateRoot).where(cb.and(predicates));
        }
        Query<GiftCertificate> query = getSession().createQuery(cr);
        query.setFirstResult((page - ONE) * size);
        query.setMaxResults(size);
        return query.getResultList();
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
        tagName = QUOTES + tagName + QUOTES;
        return getSession().createSQLQuery(String.format(SELECT_ALL_CERTIFICATES_BY_TAG_NAME, tagName, sortType, orderType)).list();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private Predicate[] getPredicatesForTags(List<Tag> tags, CriteriaBuilder cb, Root<GiftCertificate> giftCertificateRoot) {
        Predicate[] predicates = new Predicate[tags.size()];
        for (int i = 0; i < tags.size(); i++) {
            Predicate predicate = cb.isMember(tags.get(i), giftCertificateRoot.get(TAGS));
            predicates[i] = predicate;
        }
        return predicates;
    }
}
