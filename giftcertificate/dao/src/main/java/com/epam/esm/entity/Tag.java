package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * class entity Tag
 *
 * @author Pavel Veka
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id of Tag
     */
    private long id;

    /**
     * name of Tag
     */
    private String name;

    /**
     * list of GiftCertificates linked with Tag
     */
    private List<GiftCertificate> certificates = new ArrayList<>();

    /**
     * Constructor with two parameter
     *
     * @param id
     * @param name
     */
    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
