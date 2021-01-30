package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Class entity Tag
 *
 * @author Pavel Veka
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Id of Tag
     */
    private long id;

    /**
     * Name of Tag
     */
    private String name;

}
