package com.epam.esm.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.GiftCertificate;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.constant.ServiceConstant;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiftSertificateServiceImpl implements GiftCertificateService {

    private static final Logger logger = Logger.getLogger(GiftSertificateServiceImpl.class);

    @Autowired
    private GiftCertificateDAO giftCertificateDAO;

    @Autowired
    private TagDAO tagDAO;

    @Autowired
    private ModelMapper modelMapper;

    public GiftSertificateServiceImpl() {

    }


    public GiftSertificateServiceImpl(GiftCertificateDAO giftCertificateDAO, TagDAO tagDAO, ModelMapper modelMapper) {
        this.giftCertificateDAO = giftCertificateDAO;
        this.tagDAO = tagDAO;
        this.modelMapper = modelMapper;
    }


    @Transactional(rollbackFor = DuplicateEntryServiceException.class)
    @Secured("ROLE_ADMIN")
    @Override
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) throws DuplicateEntryServiceException {
        GiftCertificate certificate;
        giftCertificateDto.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()));
        giftCertificateDto.setLastUpdateDate(giftCertificateDto.getCreateDate());
        List<Tag> tags = getListTagsByNameForUpdate(giftCertificateDto.getTags());
        certificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);
        certificate.setTags(tags);
        try {
            certificate = giftCertificateDAO.save(certificate);
        } catch (Exception e) {
            throw new DuplicateEntryServiceException(ServiceConstant.KEY_DUPLICATE_CERTIFICATE);
        }
        return modelMapper.map(certificate, GiftCertificateDto.class);
    }


    @Override
    public GiftCertificateDto read(long id) throws IdNotExistServiceException {
        GiftCertificate foundCertificate;
        GiftCertificateDto giftCertificateDto;
        foundCertificate = giftCertificateDAO.readById(id);
        if (Objects.isNull(foundCertificate)) {
            throw new IdNotExistServiceException(ServiceConstant.KEY_ID_NOT_EXIST);
        }
        giftCertificateDto = modelMapper.map(foundCertificate, GiftCertificateDto.class);
        return giftCertificateDto;
    }

    @Override
    @Secured("ROLE_ADMIN")
    @Transactional
    public void update(GiftCertificateDto modifiedGiftCertificateDto) throws DuplicateEntryServiceException, IdNotExistServiceException {
        GiftCertificate giftCertificateRead = giftCertificateDAO.readById(modifiedGiftCertificateDto.getId());
        if (Objects.isNull(giftCertificateRead)) {
            throw new IdNotExistServiceException(ServiceConstant.KEY_ID_NOT_EXIST);
        }
        if (StringUtils.isNoneEmpty(modifiedGiftCertificateDto.getName())) {

            giftCertificateRead.setName(modifiedGiftCertificateDto.getName());
        }
        if (StringUtils.isNoneEmpty(modifiedGiftCertificateDto.getDescription())) {
            giftCertificateRead.setDescription(modifiedGiftCertificateDto.getDescription());
        }
        if (Objects.nonNull(modifiedGiftCertificateDto.getPrice())) {
            giftCertificateRead.setPrice(modifiedGiftCertificateDto.getPrice());
        }
        if (Objects.nonNull(modifiedGiftCertificateDto.getDuration())) {
            giftCertificateRead.setDuration(modifiedGiftCertificateDto.getDuration());
        }
        if (!modifiedGiftCertificateDto.getTags().isEmpty()) {
            giftCertificateRead.setTags(getListTagsByNameForUpdate(modifiedGiftCertificateDto.getTags()));
        }
        try {
            giftCertificateDAO.save(giftCertificateRead);
        } catch (Exception e) {
            throw new DuplicateEntryServiceException(ServiceConstant.KEY_DUPLICATE_CERTIFICATE);
        }


        logger.info("GiftCertificate has been updated in DB");
    }

    @Override
    @Secured("ROLE_ADMIN")
    @Transactional
    public void delete(long id) throws IdNotExistServiceException {
        GiftCertificate giftCertificate;
        giftCertificate = giftCertificateDAO.readById(id);
        if (Objects.nonNull(giftCertificate)) {
            giftCertificate.setDeleted(true);
            giftCertificateDAO.save(giftCertificate);
        } else {
            throw new IdNotExistServiceException(ServiceConstant.KEY_ID_NOT_EXIST);
        }
        logger.info("GiftCertificate is deleted from DB");
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> findAll(String name, String description, String[] tags, String sortType, String orderType,
                                            Integer page, Integer size)
            throws PaginationException, RequestParamServiceException {
        List<GiftCertificate> giftCertificates;
        List<GiftCertificateDto> giftCertificateDtoList;
        List<Tag> tagList;
        int checkedPage = PaginationUtil.checkPage(page);
        int checkedSize = PaginationUtil.checkSizePage(size);
        if (!name.equals(ServiceConstant.ANY_SYMBOL)) {
            name = ServiceConstant.ANY_SYMBOL + name + ServiceConstant.ANY_SYMBOL;
        }
        if (!description.equals(ServiceConstant.ANY_SYMBOL)) {
            description = ServiceConstant.ANY_SYMBOL + description + ServiceConstant.ANY_SYMBOL;
        }
        if (Objects.nonNull(tags)) {
            tagList = getListTagsByNames(tags);
        } else {
            tagList = new ArrayList<>();
        }
        try {
            giftCertificates = giftCertificateDAO.readAll(name, description, tagList, sortType, orderType, checkedPage, checkedSize);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new RequestParamServiceException(ServiceConstant.KEY_NO_SUCH_FIELD);
        }
        giftCertificateDtoList = giftCertificates.stream()
                .map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
        return giftCertificateDtoList;
    }


    private List<Tag> getListTagsByNames(String[] names) {
        List<Tag> tags = new ArrayList<>();
        if (!Objects.isNull(names)) {
            for (String tagName : names) {
                Tag tag = tagDAO.getTagByName(tagName);
                if (Objects.nonNull(tag)) {
                    tags.add(tag);
                } else {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    newTag = tagDAO.save(newTag);
                    tags.add(newTag);
                }
            }
        }
        return tags;
    }

    private List<Tag> getListTagsByNameForUpdate(List<TagDto> tagDtoList) {
        List<Tag> tags = new ArrayList<>();
        if (!Objects.isNull(tagDtoList)) {
            for (TagDto tagDto : tagDtoList) {
                Tag tag = tagDAO.getTagByName(tagDto.getName());
                if ((Objects.nonNull(tag))) {
                    if (!tags.contains(tag)) {
                        tags.add(tag);
                    }
                } else {
                    Tag newTag = new Tag();
                    newTag.setName(tagDto.getName());
                    if (!tags.contains(newTag)) {
                        tags.add(newTag);
                    }
                }
            }
        }
        return tags;
    }

}
