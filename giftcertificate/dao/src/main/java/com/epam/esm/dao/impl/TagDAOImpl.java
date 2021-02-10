package com.epam.esm.dao.impl;

import java.util.List;

import com.epam.esm.entity.GiftCertificate;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * TagJDBCTemplate - class for work with Tag
 */
@Repository
public class TagDAOImpl implements TagDAO {

    private static final String GET_TAG_BY_ID = "Tag.findById";
    private static final String GET_ALL_TAGS = "Tag.findAll";
    private static final String GET_TAGS_BY_GIFT_CERTIFICATE_ID = "Tag.findByCertificateId";
    private static final String GET_TAG_BY_NAME = "Tag.getTagByName";
    private static final String ID = "id";
    public static final String NAME_TAG="name";
    private static final int ONE=1;

    /**
     * Instance of SessionFactory for work with DB
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Create new tag in DB
     *
     * @param tag will be created in DB
     * @return created Tag
     */
    @Override
    public long create(Tag tag) throws ConstraintViolationException {
        sessionFactory.getCurrentSession().saveOrUpdate(tag);
        return tag.getId();
    }

    /**
     * Read one Tag from DB by id
     *
     * @param id Tag with this id will be read from DB
     * @return Tag
     */
    @Override
    public Tag read(long id) {
        return (Tag) getSession().getNamedQuery(GET_TAG_BY_ID).setParameter(ID, id).uniqueResult();
    }


    /**
     * Delete Tag from DB by id
     *
     * @param tag this Tag id will be deleted from DB
     */
    @Override
    public void delete(Tag tag) {
        getSession().delete(tag);

    }

    /**
     * Find all Tags
     *
     * @return list of Tags
     */
    @Override
    public List<Tag> findAll(Integer pageNumber, Integer pageSize) {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Tag> cr = cb.createQuery(Tag.class);
        Root<Tag> tagRoot = cr.from(Tag.class);
        cr.select(tagRoot).orderBy(cb.asc(tagRoot.get(ID)));
        Query<Tag> query = getSession().createQuery(cr);
        query.setFirstResult((pageNumber - ONE) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    /**
     *  Get Tags by id of certificate
     * @param certificateId id of gift certificate
     * @return list of tags
     */
    @Override
    public List<Tag> getTagsByGiftCertificateId(long certificateId) {
        return getSession().getNamedQuery(GET_TAGS_BY_GIFT_CERTIFICATE_ID).setParameter(ID, certificateId).list();
    }


    /**
     * Get Tag by name
     *
     * @param tagName name of tag
     * @return Tag
     */
    @Override
    public Tag getTagByName(String tagName) {
        return (Tag) getSession().getNamedQuery(GET_TAG_BY_NAME).setParameter(NAME_TAG, tagName).uniqueResult();
    }

    /**
     *  Get Tag widely used by user with highest cost
     * @param userId id of user
     * @return Tag
     */
    @Override
    public Tag getWidelyUsedByUserTagWithHighestCost(long userId) {
        return null;
    }


    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
