package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.IdNotExistServiceException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HATEOASBuilder {

    private static final Integer ZERO = 0;

    public static void addLinksToGiftCertificates(List<GiftCertificateDto> giftCertificateDtoList) throws IdNotExistServiceException {
        List<GiftCertificateDto> certificatesHaveLink = new ArrayList<>();
        for (GiftCertificateDto giftCertificateDto : giftCertificateDtoList) {
            if (!certificatesHaveLink.contains(giftCertificateDto)) {
                giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class).read(giftCertificateDto.getId())).withSelfRel());
                addLinksToTags(giftCertificateDto.getTags());
                certificatesHaveLink.add(giftCertificateDto);
            }
        }
    }

    public static void addLinksToGiftCertificate(GiftCertificateDto giftCertificateDto) throws IdNotExistServiceException {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class).read(giftCertificateDto.getId())).withSelfRel());
        addLinksToTags(giftCertificateDto.getTags());
    }

    public static void addLinksToTags(List<TagDto> tagDtoList) throws IdNotExistServiceException {
        if (tagDtoList.size() != ZERO) {
            for (TagDto tagDto : tagDtoList) {
                tagDto.add(linkTo(methodOn(TagController.class).readTagById(tagDto.getId())).withSelfRel());
            }
        }
    }

    public static void addLinksToTag(TagDto tagDto) throws IdNotExistServiceException {
        tagDto.add(linkTo(methodOn(TagController.class).readTagById(tagDto.getId())).withSelfRel());
    }

    public static void addLinksToUsers(List<User> userList) throws IdNotExistServiceException {
        for (User user : userList) {
            user.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
        }
    }

    public static void addLinksToUser(User user) throws IdNotExistServiceException {
        user.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
    }

    public static void addLinksToOrders(List<OrderDto> orderDtoList) throws IdNotExistServiceException {
        if (orderDtoList.size() != ZERO) {
            orderDtoList.get(0).getUser().add(linkTo(methodOn(UserController.class).getUser(orderDtoList.get(0).getUser().getId())).withSelfRel());
            for (OrderDto orderDto : orderDtoList) {
                addAllLinksToOrder(orderDto);
            }
        }

    }

    public static void addLinksToOrder(OrderDto orderDto) throws IdNotExistServiceException {
        orderDto.getUser().add(linkTo(methodOn(UserController.class).getUser(orderDto.getUser().getId())).withSelfRel());
        addAllLinksToOrder(orderDto);
    }

    private static void addAllLinksToOrder(OrderDto orderDto) throws IdNotExistServiceException {
        orderDto.add(linkTo(methodOn(OrderController.class).getOrdersById(orderDto.getOrdersId())).withSelfRel());
        List<GiftCertificateDto> certificatesHaveLink = new ArrayList<>();
        List<TagDto> tagsHaveLink = new ArrayList<>();
        for (GiftCertificateDto giftCertificateDto : orderDto.getCertificates()) {
            if (!certificatesHaveLink.contains(giftCertificateDto)) {
                giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class).read(giftCertificateDto.getId())).withSelfRel());
                for (TagDto tagDto : giftCertificateDto.getTags()) {
                    if (!tagsHaveLink.contains(tagDto)) {
                        tagDto.add(linkTo(methodOn(TagController.class).readTagById(tagDto.getId())).withSelfRel());
                        tagsHaveLink.add(tagDto);
                    }
                }
                certificatesHaveLink.add(giftCertificateDto);
            }
        }
    }
}
