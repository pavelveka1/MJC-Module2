package com.epam.esm.entity;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Class implements interface what is used by JdbcTemplate for mapping rows
 * of a ResultSet on a per-row GiftCertificate
 */
@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    /**
     * id field is mapping with id field GiftCertificate class
     */
    private static final String ID = "id";

    /**
     * name field is mapping with name field GiftCertificate class
     */
    private static final String NAME = "name";

    /**
     * description field is mapping with description field GiftCertificate class
     */
    private static final String DESCRIPTION = "description";

    /**
     * price field is mapping with price field GiftCertificate class
     */
    private static final String PRICE = "price";

    /**
     * duration field is mapping with duration field GiftCertificate class
     */
    private static final String DURATION = "duration";

    /**
     * create_date field is mapping with createDate field GiftCertificate class
     */
    private static final String CREATE_DATE = "create_date";

    /**
     * last_update_date field is mapping with lastUpdateDate field GiftCertificate class
     */
    private static final String LAST_UPDATE_DATE = "last_update_date";

    /**
     * Method for mapping data from resultSet to GiftCertificate
     *
     * @param rs
     * @param rowNum
     * @return GiftCertificate with filled fields
     * @throws SQLException
     */
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong(ID));
        giftCertificate.setName(rs.getString(NAME));
        giftCertificate.setDescription(rs.getString(DESCRIPTION));
        giftCertificate.setPrice(rs.getInt(PRICE));
        giftCertificate.setDuration(rs.getInt(DURATION));
        giftCertificate.setCreateDate(rs.getTimestamp(CREATE_DATE).toLocalDateTime());
        giftCertificate.setLastUpdateDate(rs.getTimestamp(LAST_UPDATE_DATE).toLocalDateTime());
        return giftCertificate;
    }


}
