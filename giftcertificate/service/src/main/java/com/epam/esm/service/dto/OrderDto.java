package com.epam.esm.service.dto;

import com.epam.esm.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {

    private long ordersId;
    private User user;
    private int cost;
    private String date;
    private List<GiftCertificateDto> certificates;

}
