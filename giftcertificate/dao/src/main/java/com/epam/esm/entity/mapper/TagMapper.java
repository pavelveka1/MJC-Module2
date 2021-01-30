package com.epam.esm.entity.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class implements interface what is used by JdbcTemplate for mapping rows
 * of a ResultSet on a per-row GiftCertificate
 */
@Component
public class TagMapper implements RowMapper<Tag> {

    /**
     * id field is mapping with id field Tag class
     */
    private static final String ID = "id";

    /**
     * name field is mapping with name field Tag class
     */
    private static final String NAME = "name";

    /**
     * Method for mapping data from resultSet to Tag
     *
     * @param rs     ResultSet
     * @param rowNum number of rows
     * @return Tag with filled fields
     * @throws SQLException if ResultSet don't contains appropriate value
     */
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tag(rs.getLong(ID), rs.getString(NAME));
    }


}
