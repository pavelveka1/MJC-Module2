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


    private static List<GiftCertificateDto> certificatesHaveLink = new ArrayList<>();

    public static void addLinksToGiftCertificates(List<GiftCertificateDto> giftCertificateDtoList) throws IdNotExistServiceException {
        List<TagDto> tagsHaveLink = new ArrayList<>();
        for (GiftCertificateDto giftCertificateDto : giftCertificateDtoList) {
            if (!certificatesHaveLink.contains(giftCertificateDto)) {
                giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class).read(giftCertificateDto.getId())).withSelfRel());
                addLinksToTagsInCertificate(giftCertificateDto.getTags(), tagsHaveLink);
                certificatesHaveLink.add(giftCertificateDto);
            }
        }
    }

    public static void addLinksToGiftCertificate(GiftCertificateDto giftCertificateDto) throws IdNotExistServiceException {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class).read(giftCertificateDto.getId())).withSelfRel());
        addLinksToTags(giftCertificateDto.getTags());
    }


    public static void addLinksToTagsInCertificate(List<TagDto> tagDtoList, List<TagDto> tagsHaveLink) throws IdNotExistServiceException {
        for (TagDto tagDto : tagDtoList) {
            if (!tagsHaveLink.contains(tagDto)) {
                tagDto.add(linkTo(methodOn(TagController.class).readTagById(tagDto.getId())).withSelfRel());
                tagsHaveLink.add(tagDto);
            }
        }
    }

    public static void addLinksToTags(List<TagDto> tagDtoList) throws IdNotExistServiceException {
        for (TagDto tagDto : tagDtoList) {
            tagDto.add(linkTo(methodOn(TagController.class).readTagById(tagDto.getId())).withSelfRel());
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
        for (OrderDto orderDto : orderDtoList) {
            orderDto.add(linkTo(methodOn(OrderController.class).getOrdersById(orderDto.getOrders_id())).withSelfRel());
            addLinksToGiftCertificates(orderDto.getCertificates());
        }
    }

    public static void addLinksToOrder(OrderDto orderDto) throws IdNotExistServiceException {
        orderDto.add(linkTo(methodOn(OrderController.class).getOrdersById(orderDto.getOrders_id())).withSelfRel());
        addLinksToGiftCertificates(orderDto.getCertificates());

    }
}
