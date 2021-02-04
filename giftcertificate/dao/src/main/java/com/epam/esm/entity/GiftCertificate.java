package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

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
     * Id of GiftCertificate
     */
    private long id;

    /**
     * Name of GiftCertificate
     */
    private String name;

    /**
     * Description of GiftCertificate
     */
    private String description;

    /**
     * Price of GiftCertificate
     */
    private Integer price;

    /**
     * Duratuin in days
     */
    private Integer duration;

    /**
     * Date of creation of GiftCertificate
     */
    private LocalDateTime createDate;

    /**
     * Last date of updating of GiftCertificate
     */
    private LocalDateTime lastUpdateDate;

}
