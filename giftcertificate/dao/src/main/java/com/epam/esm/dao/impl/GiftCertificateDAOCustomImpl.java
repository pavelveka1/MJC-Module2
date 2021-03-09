package com.epam.esm.dao.impl;

import com.epam.esm.constant.DAOConstant;
import com.epam.esm.dao.GiftCertificateDAOCustom;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

public class GiftCertificateDAOCustomImpl implements GiftCertificateDAOCustom {

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
        query.setFirstResult((page - DAOConstant.ONE) * size);
        query.setMaxResults(size);
        return query.getResultList();

    }

    private Predicate[] getPredicatesForTags(List<Tag> tags, CriteriaBuilder cb, Root<GiftCertificate> giftCertificateRoot) {
        Predicate[] predicates = new Predicate[tags.size()];
        for (int i = DAOConstant.ZERO; i < tags.size(); i++) {
            Predicate predicate = cb.like(giftCertificateRoot.join(DAOConstant.TAGS).get(DAOConstant.NAME),
                    DAOConstant.ANY_SYMBOLS + tags.get(i).getName() + DAOConstant.ANY_SYMBOLS);
            predicates[i] = predicate;
        }
        return predicates;
    }

    private Predicate[] getSuitablePredicates(String name, String description, List<Tag> tags,
                                              CriteriaBuilder criteriaBuilder, Root<GiftCertificate> giftCertificateRoot) {
        Predicate predicateNotDeleted = criteriaBuilder.notEqual(giftCertificateRoot.get(DAOConstant.DELETED), true);
        Predicate predicateLikeName = criteriaBuilder.like(giftCertificateRoot.get(DAOConstant.NAME), name);
        Predicate predicateLikeDescription = criteriaBuilder.like(giftCertificateRoot.get(DAOConstant.DESCRIPTION), description);
        Predicate[] predicatesForTags = getPredicatesForTags(tags, criteriaBuilder, giftCertificateRoot);
        Predicate[] predicates = new Predicate[predicatesForTags.length + DAOConstant.THREE];
        predicates[DAOConstant.ZERO] = predicateNotDeleted;
        predicates[DAOConstant.ONE] = predicateLikeName;
        predicates[DAOConstant.TWO] = predicateLikeDescription;
        for (int i = DAOConstant.THREE; i < predicates.length; i++) {
            predicates[i] = predicatesForTags[i - DAOConstant.THREE];
        }
        return predicates;
    }

    private void setOrderingAndPredicates(String sortType, String orderType, CriteriaBuilder cb,
                                          Root<GiftCertificate> giftCertificateRoot, Predicate[] predicates,
                                          CriteriaQuery<GiftCertificate> criteriaQuery) {
        if (orderType.equals(DAOConstant.DESC)) {
            criteriaQuery.select(giftCertificateRoot).where(cb.and(predicates))
                    .orderBy(cb.desc(giftCertificateRoot.get(sortType)));
        } else {
            criteriaQuery.select(giftCertificateRoot).where(cb.and(predicates))
                    .orderBy(cb.asc(giftCertificateRoot.get(sortType)));
        }
    }
}
