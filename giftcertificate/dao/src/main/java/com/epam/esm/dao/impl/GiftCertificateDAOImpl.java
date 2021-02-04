package com.epam.esm.dao.impl;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.mapper.GiftCertificateMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.mapper.TagMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * GiftSertificateJDBCTemplate - class for work with GiftCertificate
 */
@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private static final Logger logger = Logger.getLogger(GiftCertificateDAOImpl.class);
    private static final String SELECT_GIFT_CERTIFICATES_BY_ID = "select id, name, description, price, duration, " +
            "create_date, last_update_date from gift_certificates where id=?";
    private static final String SELECT_ALL_CERTIFICATES_WITH_SORT = "select id, name, description, price, duration," +
            "create_date, last_update_date from gift_certificates order by %s %s";
    private static final String SELECT_ALL_CERTIFICATES_BY_TAG_NAME = "select gc.id, gc.name, gc.description, gc.price, " +
            "gc.duration, gc.create_date, gc.last_update_date from gift_certificates as gc\n" +
            "\t join gift_certificates_has_tags on gc.id=gift_certificates_has_tags.gift_certificates_id\n" +
            "     join tags on tags.id=gift_certificates_has_tags.tags_id\n" +
            "     where tags.name=%s order by %s %s";
    private static final String SELECT_ALL_CERTIFICATES_BY_NAME_OR_DESCRIPTION = "select gc.id, gc.name, gc.description," +
            " gc.price, gc.duration, gc.create_date, gc.last_update_date from gift_certificates as gc\n" +
            "     where gc.name like %s order by %s %s";
    private static final String INSERT_CERTIFICATE = "INSERT INTO gift_certificates (name, description, price, duration, " +
            "create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String CREATE_CERTIFICATE_HAS_TAG = "INSERT INTO gift_certificates_has_tags (gift_certificates_id," +
            " tags_id) VALUES (?, ?)";
    private static final String UPDATE_GIFT_CERTIFICATE = "UPDATE gift_certificates SET name = ?, description = ?," +
            " price = ?, duration = ?, last_update_date = ? WHERE (id = ?)";
    private static final String DELETE_GIFT_CERTIFICATE = "DELETE FROM gift_certificates WHERE id = ?";
    private static final String ANY_CHARACTERS_BEFORE = "\"%";
    private static final String ANY_CHARACTERS_AFTER = "%\"";
    private static final String QUOTES = "\"";

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
     * Create GiftCertificate in DB
     *
     * @param giftCertificate we wont create in DB
     * @return created GiftCertificate
     * @throws DataIntegrityViolationException if this GiftCertificate already exists in the DB
     */
    @Override
    public long create(GiftCertificate giftCertificate) throws DuplicateKeyException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, giftCertificate.getName());
            preparedStatement.setString(2, giftCertificate.getDescription());
            preparedStatement.setInt(3, giftCertificate.getPrice());
            preparedStatement.setInt(4, giftCertificate.getDuration());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
            return preparedStatement;
        }, keyHolder);
        logger.info("GiftCertificate is created in DB");
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    /**
     * Read GiftCertificate from DB by id
     *
     * @param id long type parameter
     * @return Optional<GiftCertificate>
     * @throws EmptyResultDataAccessException if records with such id not exist in DB
     */
    @Override
    public GiftCertificate read(long id) throws EmptyResultDataAccessException {
        GiftCertificate giftCertificate = jdbcTemplate.queryForObject(SELECT_GIFT_CERTIFICATES_BY_ID, new Object[]{id},
                giftCertificateMapper);
        logger.info("GiftCertificate is read from DB");
        return giftCertificate;
    }

    /**
     * Update GiftCertificate
     *
     * @param giftCertificate we wont update
     * @return updated GiftCertificate
     */
    @Override
    public int update(GiftCertificate giftCertificate) {
        return jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                LocalDateTime.now(ZoneId.systemDefault()),
                giftCertificate.getId());
    }

    /**
     * Delete GiftCertificate from DB by id
     *
     * @param id GiftCertificate with this id will be deleted from DB
     */
    @Override
    public int delete(long id) {
        return jdbcTemplate.update(DELETE_GIFT_CERTIFICATE, id);
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
        return jdbcTemplate.query(String.format(SELECT_ALL_CERTIFICATES_WITH_SORT, sortType, orderType), giftCertificateMapper);
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
        return jdbcTemplate.query(String.format(SELECT_ALL_CERTIFICATES_BY_TAG_NAME, tagName, sortType, orderType),
                giftCertificateMapper);
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
        nameOrDescription = ANY_CHARACTERS_BEFORE + nameOrDescription + ANY_CHARACTERS_AFTER;
        return jdbcTemplate.query(String.format(SELECT_ALL_CERTIFICATES_BY_NAME_OR_DESCRIPTION, nameOrDescription,
                sortType, orderType), giftCertificateMapper);

    }


    /**
     * Method attach tags to gift certificate
     *
     * @param idGiftCertificate id of gift certificate
     * @param tags              list of tags
     * @throws DataIntegrityViolationException exception
     */
    @Override
    public void attachTags(long idGiftCertificate, List<Tag> tags) throws DataIntegrityViolationException {
        batchUpdateGiftCertificateHasTg(idGiftCertificate, tags);
    }

    private void batchUpdateGiftCertificateHasTg(long idGiftCertificate, List<Tag> tags) {
        jdbcTemplate.batchUpdate(
                CREATE_CERTIFICATE_HAS_TAG,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setLong(1, idGiftCertificate);
                        ps.setLong(2, tags.get(i).getId());
                    }

                    public int getBatchSize() {
                        return tags.size();
                    }
                });
    }

}
