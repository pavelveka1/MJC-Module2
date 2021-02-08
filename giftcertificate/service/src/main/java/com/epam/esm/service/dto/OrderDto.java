package com.epam.esm.service.dto;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private long orders_id;
    private User user;
    private int cost;
    private String date;
    private List<GiftCertificate> certificates;

}
