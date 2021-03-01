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
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class GiftCertificateService
 * Contains methods for work with GiftCertificateDto
 */
@Service
public class GiftSertificateServiceImpl implements GiftCertificateService {

    private static final Logger logger = Logger.getLogger(GiftSertificateServiceImpl.class);
    private static final String ANY_SYMBOL = "%";
    private static final int ZERO = 0;
    private static final String KEY_NO_SUCH_FIELD = "certificate_no_such_field";
    private static final String KEY_DUPLICATE_CERTIFICATE = "certificate_duplicate";
    private static final String KEY_ID_NOT_EXIST = "certificate_no_id";

    /**
     * GiftSertificateJDBCTemplate is used for operations with GiftCertificate
     */
    @Autowired
    private GiftCertificateDAO giftCertificateDAO;

    /**
     * TagJDBCTemplate is used for operations with Tag
     */
    @Autowired
    private TagDAO tagDAO;

    /**
     * ModelMapper is used for convertation TagDto to Tag or GiftCertificateDto to GiftCertificate
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Empty constructor
     */
    public GiftSertificateServiceImpl() {

    }

    /**
     * Constcuctor with all parameters
     *
     * @param giftCertificateDAO for operations with GiftCertivicate
     * @param tagDAO             for operations with Tag
     * @param modelMapper        for convertion object
     */
    public GiftSertificateServiceImpl(GiftCertificateDAO giftCertificateDAO, TagDAO tagDAO, ModelMapper modelMapper) {
        this.giftCertificateDAO = giftCertificateDAO;
        this.tagDAO = tagDAO;
        this.modelMapper = modelMapper;
    }

    /**
     * Create GiftCertificate in DB
     *
     * @param giftCertificateDto it contains data for creation giftCertificate
     * @return created GiftCertificate as GiftCertificateDto
     * @throws DuplicateEntryServiceException if this GiftCertificate already exists in the DB
     */
    @Transactional(rollbackFor = DuplicateEntryServiceException.class)
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
            throw new DuplicateEntryServiceException(KEY_DUPLICATE_CERTIFICATE);
        }
        return modelMapper.map(certificate, GiftCertificateDto.class);
    }

    /**
     * Read GiftCertificateDto from DB by id
     *
     * @param id id of GiftCertificate
     * @return GiftCertificateDto
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    @Override
    public GiftCertificateDto read(long id) throws IdNotExistServiceException {
        GiftCertificate foundCertificate;
        GiftCertificateDto giftCertificateDto;
        foundCertificate = giftCertificateDAO.readById(id);
        if (Objects.isNull(foundCertificate)) {
            throw new IdNotExistServiceException(KEY_ID_NOT_EXIST);
        }
        giftCertificateDto = modelMapper.map(foundCertificate, GiftCertificateDto.class);
        return giftCertificateDto;
    }

    /**
     * Update GiftCertificate as GiftCertificateDto
     *
     * @param modifiedGiftCertificateDto modified GiftCertificate
     */
    @Override
    @Transactional
    public void update(GiftCertificateDto modifiedGiftCertificateDto) throws DuplicateEntryServiceException, IdNotExistServiceException {
        GiftCertificate giftCertificateRead = giftCertificateDAO.readById(modifiedGiftCertificateDto.getId());
        if (Objects.isNull(giftCertificateRead)) {
            throw new IdNotExistServiceException(KEY_ID_NOT_EXIST);
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
            //  giftCertificateRead.setTags(modifiedGiftCertificateDto.getTags().stream().map(tagDto -> modelMapper.map(tagDto, Tag.class)).collect(Collectors.toList()));
        }
        giftCertificateDAO.save(giftCertificateRead);
        logger.info("GiftCertificate has been updated in DB");
    }

    /**
     * Delete GiftCertificate from DB by id
     *
     * @param id id of GiftCertificate
     * @throws IdNotExistServiceException if record with such id not exist in DB
     */
    @Override
    @Transactional
    public void delete(long id) throws IdNotExistServiceException {
        GiftCertificate giftCertificate;
        giftCertificate = giftCertificateDAO.readById(id);
        if (Objects.nonNull(giftCertificate)) {
            giftCertificate.setDeleted(true);
            giftCertificateDAO.save(giftCertificate);
        } else {
            throw new IdNotExistServiceException(KEY_ID_NOT_EXIST);
        }
        logger.info("GiftCertificate is deleted from DB");
    }

    /**
     * Find all giftCertificates with condition determined by parameters
     *
     * @param sortType  name of field of table in DB
     * @param orderType ASC or DESC
     * @return list og GiftCertificates
     * @throws RequestParamServiceException if parameters don't right
     * @throws PaginationException          if page equals zero
     */
    @Override
    @Transactional
    public List<GiftCertificateDto> findAll(String name, String description, String[] tags, String sortType, String orderType,
                                            Integer page, Integer size)
            throws PaginationException, RequestParamServiceException {
        List<GiftCertificate> giftCertificates;
        List<GiftCertificateDto> giftCertificateDtoList;
        List<Tag> tagList;
        page = PaginationUtil.checkPage(page);
        size = PaginationUtil.checkSizePage(size);
        if (!name.equals(ANY_SYMBOL)) {
            name = ANY_SYMBOL + name + ANY_SYMBOL;
        }
        if (!description.equals(ANY_SYMBOL)) {
            description = ANY_SYMBOL + description + ANY_SYMBOL;
        }

        if (Objects.nonNull(tags)) {
            tagList = getListTagsByNames(tags);
        } else {
            tagList = new ArrayList<>();
        }
        try {
            giftCertificates = giftCertificateDAO.readAll(name, description, tagList, sortType, orderType, page, size);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new RequestParamServiceException(KEY_NO_SUCH_FIELD);
        }
        giftCertificateDtoList = giftCertificates.stream()
                .map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
        return giftCertificateDtoList;
    }


    private List<Tag> getListTagsByNames(String[] names) {
        List<Tag> tags = new ArrayList<>();
        if (!Objects.isNull(names)) {
            for (int i = ZERO; i < names.length; i++) {
                Tag tag = tagDAO.getTagByName((names[i]));
                if (Objects.nonNull(tag)) {
                    tags.add(tag);
                } else {
                    Tag newTag = new Tag();
                    newTag.setName(names[i]);
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
            for (int i = ZERO; i < tagDtoList.size(); i++) {
                Tag tag = tagDAO.getTagByName(tagDtoList.get(i).getName());
                if ((Objects.nonNull(tag))) {
                    if (!tags.contains(tag)) {
                        tags.add(tag);
                    }
                } else {
                    Tag newTag = new Tag();
                    newTag.setName(tagDtoList.get(i).getName());
                    if (!tags.contains(newTag)) {
                        tags.add(newTag);
                    }
                }
            }
        }
        return tags;
    }

}
