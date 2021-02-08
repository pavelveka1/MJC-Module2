package com.epam.esm.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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


    private static final String CREATE_TAG = "INSERT INTO tags (name) VALUES (?)";

    private static final String DELETE_CERTIFICATE_HAS_TAG = "DELETE FROM gift_certificates_has_tags WHERE gift_certificates_id=?" +
            " and tags_id=?";
    private static final String CREATE_CERTIFICATE_HAS_TAG = "INSERT INTO gift_certificates_has_tags (gift_certificates_id," +
            " tags_id) VALUES (?, ?)";
    private static final Integer PARAMETER_INDEX_TAG_NAME = 1;


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
    public List<Tag> findAll() {
        return getSession().getNamedQuery(GET_ALL_TAGS).list();
    }

    @Override
    public List<Tag> getTagsByGiftCertificateId(long certificateId) {
        return getSession().getNamedQuery(GET_TAGS_BY_GIFT_CERTIFICATE_ID).setParameter(ID, certificateId).list();
    }

    @Override
    public void updateListTagsForCertificate(long idCertificate, List<Tag> newTags, List<Tag> oldTags) {
        // получаем сертификат и через setter устанавливаем новый список тэгов и вызываес save

        /*
        deleteOldTags(idCertificate, oldTags);
        setNewTags(idCertificate, newTags);

         */
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

    @Override
    public Tag getWidelyUsedByUserTagWithHighestCost(long userId) {
        return null;
    }

   /*
    private void deleteOldTags(long idGiftCertificate, List<Tag> oldTags) {

        jdbcTemplate.batchUpdate(
                DELETE_CERTIFICATE_HAS_TAG,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setLong(1, idGiftCertificate);
                        ps.setLong(2, oldTags.get(i).getId());
                    }

                    public int getBatchSize() {
                        return oldTags.size();
                    }
                });
    }

    private void setNewTags(long idGiftCertificate, List<Tag> newTags) {
        jdbcTemplate.batchUpdate(
                CREATE_CERTIFICATE_HAS_TAG,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setLong(1, idGiftCertificate);
                        ps.setLong(2, newTags.get(i).getId());
                    }

                    public int getBatchSize() {
                        return newTags.size();
                    }
                });
    }


    */

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
