package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAOCustom;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class GiftCertificateDAOCustomImpl implements GiftCertificateDAOCustom {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAGS = "tags";
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int ZERO = 0;
    private static final String DELETED = "deleted";
    private static final String DESC = "desc";

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<GiftCertificate> readAll(String name, String description, List<Tag> tags, String sortType,
                                         String orderType, Integer page, Integer size) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        Predicate[] predicates = getSuitablePredicates(name, description, tags, criteriaBuilder, giftCertificateRoot);
        setOrderingAndPredicates(sortType, orderType, criteriaBuilder, giftCertificateRoot, predicates, criteriaQuery);
        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((page - ONE) * size);
        query.setMaxResults(size);
        return query.getResultList();

    }

    private Predicate[] getPredicatesForTags(List<Tag> tags, CriteriaBuilder cb, Root<GiftCertificate> giftCertificateRoot) {
        Predicate[] predicates = new Predicate[tags.size()];
        for (int i = ZERO; i < tags.size(); i++) {
            Predicate predicate = cb.isMember(tags.get(i), giftCertificateRoot.get(TAGS));
            predicates[i] = predicate;
        }
        return predicates;
    }

    private Predicate[] getSuitablePredicates(String name, String description, List<Tag> tags,
                                              CriteriaBuilder criteriaBuilder, Root<GiftCertificate> giftCertificateRoot) {
        Predicate predicateNotDeleted = criteriaBuilder.notEqual(giftCertificateRoot.get(DELETED), true);
        Predicate predicateLikeName = criteriaBuilder.like(giftCertificateRoot.get(NAME), name);
        Predicate predicateLikeDescription = criteriaBuilder.like(giftCertificateRoot.get(DESCRIPTION), description);
        Predicate[] predicatesForTags = getPredicatesForTags(tags, criteriaBuilder, giftCertificateRoot);
        Predicate[] predicates = new Predicate[predicatesForTags.length + THREE];
        predicates[ZERO] = predicateNotDeleted;
        predicates[ONE] = predicateLikeName;
        predicates[TWO] = predicateLikeDescription;
        for (int i = THREE; i < predicates.length; i++) {
            predicates[i] = predicatesForTags[i - THREE];
        }
        return predicates;
    }

    private void setOrderingAndPredicates(String sortType, String orderType, CriteriaBuilder cb,
                                          Root<GiftCertificate> giftCertificateRoot, Predicate[] predicates,
                                          CriteriaQuery<GiftCertificate> criteriaQuery) {
        if (orderType.equals(DESC)) {
            criteriaQuery.select(giftCertificateRoot).where(cb.and(predicates))
                    .orderBy(cb.desc(giftCertificateRoot.get(sortType)));
        } else {
            criteriaQuery.select(giftCertificateRoot).where(cb.and(predicates))
                    .orderBy(cb.asc(giftCertificateRoot.get(sortType)));
        }
    }
}
