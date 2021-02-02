package com.epam.esm.dao.impl;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.mapper.GiftCertificateMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * TagJDBCTemplate - class for work with Tag
 */
@Repository
public class TagDAOImpl implements TagDAO {

    private static final String GET_TAG_BY_ID = "select id, name from tags where id=?";
    private static final String GET_ALL_TAGS = "select id, name from tags order by id asc";
    private static final String CREATE_TAG = "INSERT INTO tags (name) VALUES (?)";
    private static final String DELETE_TAG = "DELETE FROM tags WHERE tags.id=?";
    private static final String GET_TAGS_BY_GIFT_CERTIFICATE_ID = "select tags.id, tags.name from tags\n" +
            "\t join gift_certificates_has_tags on tags.id=gift_certificates_has_tags.tags_id\n" +
            "     where gift_certificates_has_tags.gift_certificates_id=?";
    private static final Integer PARAMETER_INDEX_TAG_NAME = 1;


    /**
     * Instance of JdbcTemplate for work with DB
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Instance of GiftCertificateMapper for mapping data from resultSet
     */
    @Autowired
    private GiftCertificateMapper giftCertificateMapper;

    /**
     * Instance of TagMapper for mapping data from resultSet
     */
    @Autowired
    private TagMapper tagMapper;

    /**
     * Create new tag in DB
     *
     * @param tag will be created in DB
     * @return created Tag
     */
    @Override
    public long create(Tag tag) throws DuplicateKeyException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TAG, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(PARAMETER_INDEX_TAG_NAME, tag.getName());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    /**
     * Read one Tag from DB by id
     *
     * @param id Tag with this id will be read from DB
     * @return Tag
     * @throws EmptyResultDataAccessException if records with such id not exist in DB
     */
    @Override
    public Tag read(long id) throws EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject(GET_TAG_BY_ID, new Object[]{id}, tagMapper);
    }

    /**
     * Delete Tag from DB by id
     *
     * @param id Tag with this id will be deleted from DB
     */
    @Override
    public int delete(long id) {
        return jdbcTemplate.update(DELETE_TAG, id);
    }

    /**
     * Find all Tags
     *
     * @return list of Tags
     */
    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(GET_ALL_TAGS, tagMapper);
    }

    @Override
    public List<Tag> getTagsByGiftCertificateId(long certificateId) {
        return jdbcTemplate.query(GET_TAGS_BY_GIFT_CERTIFICATE_ID, new Object[]{certificateId}, tagMapper);
    }

}
