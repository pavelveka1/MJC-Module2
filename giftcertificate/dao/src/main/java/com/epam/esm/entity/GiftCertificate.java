package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class entity GiftCertificate
 *
 * @author Pavel Veka
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id of GiftCertificate
     */
    private long id;

    /**
     * name of GiftCertificate
     */
    private String name;

    /**
     * description of GiftCertificate
     */
    private String description;

    /**
     * price of GiftCertificate
     */
    private Integer price;

    /**
     * duratuin in days
     */
    private Integer duration;

    /**
     * date of creation of GiftCertificate
     */
    private LocalDateTime createDate;

    /**
     * last date of updating of GiftCertificate
     */
    private LocalDateTime lastUpdateDate;

    /**
     * list of tags linked with GiftCertificate
     */
    private List<Tag> tags = new ArrayList<>();

}
