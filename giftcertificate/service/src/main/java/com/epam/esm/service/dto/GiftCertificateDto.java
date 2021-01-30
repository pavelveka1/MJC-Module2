package com.epam.esm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class GiftCertificateDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateDto {
    private int id;
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<TagDto> tags = new ArrayList<>();

}
