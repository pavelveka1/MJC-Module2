package com.epam.esm.dao.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    private static final String SELECT_GIFT_CERTIFICATES_BY_NAME = "GiftCertificate.findByName";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAG = "tag";
    private static final String ANY_CHARACTERS = "%";
    private static final String TAGS = "tags";
    private static final int ONE = 1;
    private static final int ZERO = 0;
    private static final String DELETED = "deleted";
    private static final String ASC = "asc";
    private static final String DESC = "desc";


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
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cr = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = cr.from(GiftCertificate.class);
        Predicate predicateId = criteriaBuilder.equal(giftCertificateRoot.get(ID), id);
        Predicate predicateDeleted = criteriaBuilder.equal(giftCertificateRoot.get(DELETED), false);
        cr.select(giftCertificateRoot).where(criteriaBuilder.and(predicateId, predicateDeleted));
        Query<GiftCertificate> query = getSession().createQuery(cr);
        return query.uniqueResult();
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
     * Read by name which is not deleted
     *
     * @param certificateName Name of certificate
     * @return GiftCertificate
     */
    @Override
    public GiftCertificate readByNotDeletedName(String certificateName) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cr = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = cr.from(GiftCertificate.class);
        Predicate predicateId = criteriaBuilder.equal(giftCertificateRoot.get(NAME), certificateName);
        Predicate predicateNotDeleted = criteriaBuilder.equal(giftCertificateRoot.get(DELETED), false);
        cr.select(giftCertificateRoot).where(criteriaBuilder.and(predicateId, predicateNotDeleted));
        Query<GiftCertificate> query = getSession().createQuery(cr);
        return query.uniqueResult();
    }


    /**
     * Update GiftCertificate
     *
     * @param giftCertificate we wont update
     */
    @Override
    public void update(GiftCertificate giftCertificate) {
        giftCertificate.setLastUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));
        getSession().save(giftCertificate);
    }

    /**
     * Delete GiftCertificate from DB by id
     *
     * @param giftCertificate - GiftCertificate with id will be deleted from DB
     */
    @Override
    public void delete(GiftCertificate giftCertificate) {
        giftCertificate.setDeleted(true);
        getSession().save(giftCertificate);
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
    public List<GiftCertificate> findAll(String search, List<Tag> tags, String nameOrDescription, String sortType,
                                         String orderType, Integer page, Integer size) throws BadSqlGrammarException {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        Predicate[] predicates = getSuitablePredicates(search, tags, nameOrDescription, criteriaBuilder, giftCertificateRoot);
        setOrderingAndPredicates(sortType, orderType, criteriaBuilder, giftCertificateRoot, predicates, criteriaQuery);
        Query<GiftCertificate> query = getSession().createQuery(criteriaQuery);
        query.setFirstResult((page - ONE) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private Predicate[] getPredicatesForTags(List<Tag> tags, CriteriaBuilder cb, Root<GiftCertificate> giftCertificateRoot) {
        Predicate predicateNotDeleted = cb.notEqual(giftCertificateRoot.get("deleted"), true);
        Predicate[] predicates = new Predicate[tags.size() + ONE];
        for (int i = ZERO; i < tags.size(); i++) {
            Predicate predicate = cb.isMember(tags.get(i), giftCertificateRoot.get(TAGS));
            predicates[i] = predicate;
        }
        predicates[tags.size()] = predicateNotDeleted;
        return predicates;
    }

    private Predicate[] getSuitablePredicates(String search, List<Tag> tags, String nameOrDescription,
                                              CriteriaBuilder criteriaBuilder, Root<GiftCertificate> giftCertificateRoot) {
        Predicate predicateNotDeleted = criteriaBuilder.notEqual(giftCertificateRoot.get("deleted"), true);
        Predicate predicate;
        Predicate[] predicates = new Predicate[]{};
        if (search.equals(NAME)) {
            predicate = criteriaBuilder.like(giftCertificateRoot.get(NAME), ANY_CHARACTERS + nameOrDescription + ANY_CHARACTERS);
            predicates = new Predicate[]{predicateNotDeleted, predicate};
        } else if (search.equals(DESCRIPTION)) {
            predicate = criteriaBuilder.like(giftCertificateRoot.get(DESCRIPTION), ANY_CHARACTERS + nameOrDescription + ANY_CHARACTERS);
            predicates = new Predicate[]{predicateNotDeleted, predicate};
        } else if (search.equals(TAG)) {
            predicates = getPredicatesForTags(tags, criteriaBuilder, giftCertificateRoot);
        }
        return predicates;
    }

    private void setOrderingAndPredicates(String sortType, String orderType, CriteriaBuilder cb,
                                          Root<GiftCertificate> giftCertificateRoot, Predicate[] predicates,
                                          CriteriaQuery<GiftCertificate> criteriaQuery) {
        if (orderType.equals(DESC)) {
            criteriaQuery.select(giftCertificateRoot).where(cb.and(predicates)).orderBy(cb.desc(giftCertificateRoot.get(sortType)));
        } else {
            criteriaQuery.select(giftCertificateRoot).where(cb.and(predicates)).orderBy(cb.asc(giftCertificateRoot.get(sortType)));
        }
    }
}
