package com.epam.esm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.GiftCertificate;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.*;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class GiftCertificateService
 * Contains methods for work with GiftCertificateDto
 */
@Service
public class GiftSertificateServiceImpl implements GiftCertificateService {

    private static final Logger logger = Logger.getLogger(GiftSertificateServiceImpl.class);
    private static final String DEFAULT_SORT_ORDER = "asc";
    private static final String DEFAULT_SORT_TYPE = "id";
    private static final String SEARCH_BY_TAG = "tag";
    private static final String SEARCH_BY_NAME = "name";
    private static final String SEARCH_BY_DESCRIPTION = "description";
    private static final String UNDERSCORE = "_";
    private static final String WHITESPACE = " ";

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
    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) throws DuplicateEntryServiceException {
        GiftCertificate createdGiftCertificate;
        long id;
        try {
            id = giftCertificateDAO.create(modelMapper.map(giftCertificateDto, GiftCertificate.class));
            createdGiftCertificate = giftCertificateDAO.read(id);
            List<Tag> tags = giftCertificateDto.getTags().stream()
                    .map(tagDto -> modelMapper.map(tagDto, Tag.class))
                    .collect(Collectors.toList());
            giftCertificateDAO.attachTags(createdGiftCertificate.getId(), tags);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryServiceException("Gift certificate with same name or description alredy exist");
        }
        return modelMapper.map(createdGiftCertificate, GiftCertificateDto.class);
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
        List<TagDto> tagsDto;
        try {
            foundCertificate = giftCertificateDAO.read(id);
            List<Tag> tags = tagDAO.getTagsByGiftCertificateId(foundCertificate.getId());
            tagsDto = tags.stream()
                    .map(tag -> modelMapper.map(tag, TagDto.class))
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            throw new IdNotExistServiceException("There is no GiftCertificate with id = " + id + " in DB");
        }
        giftCertificateDto = modelMapper.map(foundCertificate, GiftCertificateDto.class);
        giftCertificateDto.setTags(tagsDto);
        return giftCertificateDto;
    }

    /**
     * Update GiftCertificate as GiftCertificateDto
     *
     * @param modifiedGiftCertificateDto modified GiftCertificate
     * @return updated GiftCertificateDto
     */
    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto modifiedGiftCertificateDto) throws IdNotExistServiceException,
            UpdateServiceException {
        GiftCertificate modifiedGiftCertificate = modelMapper.map(modifiedGiftCertificateDto, GiftCertificate.class);
        List<Tag> tags = modifiedGiftCertificateDto.getTags().stream().map(tagDto -> modelMapper.map(tagDto, Tag.class))
                .collect(Collectors.toList());
        List<Tag> outdatedTags = getListOutdatedTags(modifiedGiftCertificateDto.getId(), tags);
        List<Tag> tagsNeedAttach = getListTagsNeedAttach(modifiedGiftCertificateDto.getId(), tags);
        tagDAO.updateListTagsForCertificate(modifiedGiftCertificateDto.getId(), tagsNeedAttach, outdatedTags);
        int i = giftCertificateDAO.update(modifiedGiftCertificate);
        if (i == 0) {
            logger.info("GiftCertificate is not updated in DB");
            throw new UpdateServiceException("giftCertificate has not been updated");
        }
        logger.info("GiftCertificate has been updated in DB");
        return read(modifiedGiftCertificateDto.getId());
    }

    /**
     * Delete GiftCertificate from DB by id
     *
     * @param id id of GiftCertificate
     * @throws IdNotExistServiceException if record with such id not exist in DB
     */
    @Override
    public void delete(long id) throws IdNotExistServiceException {
        int i = giftCertificateDAO.delete(id);
        if (i == 0) {
            logger.info("GiftCertificate isn't deleted from DB");
            throw new IdNotExistServiceException("GiftCertificate with id = " + id + " does not exist in DB");
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
     */
    @Override
    public List<GiftCertificateDto> findAll(String search, String value, String sortType, String orderType)
            throws RequestParamServiceException {
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        List<GiftCertificateDto> giftCertificateDtoList = null;
        try {
            if (search != null) {
                giftCertificateDtoList = searchCertificates(search, value, sortType, orderType);
            } else {
                if (sortType == null) {
                    giftCertificates = giftCertificateDAO.findAll(DEFAULT_SORT_TYPE, DEFAULT_SORT_ORDER);
                } else {
                    if (orderType == null) {
                        orderType = DEFAULT_SORT_ORDER;
                    }
                    giftCertificates = giftCertificateDAO.findAll(sortType, orderType);
                }
            }
        } catch (BadSqlGrammarException e) {
            throw new RequestParamServiceException("Passed parameters don't match with allowable");
        }
        if (giftCertificateDtoList == null) {
            giftCertificateDtoList = giftCertificates.stream()
                    .map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
                    .collect(Collectors.toList());
        }
        setTags(giftCertificateDtoList);
        return giftCertificateDtoList;
    }

    private void setTags(List<GiftCertificateDto> giftCertificateDtoList) {
        for (GiftCertificateDto giftCertificateDto : giftCertificateDtoList) {
            List<Tag> tags = tagDAO.getTagsByGiftCertificateId(giftCertificateDto.getId());
            List<TagDto> tagsDto = tags.stream()
                    .map(tag -> modelMapper.map(tag, TagDto.class))
                    .collect(Collectors.toList());
            giftCertificateDto.setTags(tagsDto);
        }
    }

    private List<GiftCertificateDto> searchCertificates(String search, String value, String sortType, String orderType)
            throws RequestParamServiceException {
        List<GiftCertificate> giftCertificateList = new ArrayList<>();
        if (sortType == null) {
            sortType = DEFAULT_SORT_TYPE;
        }
        if (orderType == null) {
            orderType = DEFAULT_SORT_ORDER;
        }
        if (search != null & value != null) {
            if (search.equals(SEARCH_BY_TAG)) {
                giftCertificateList = giftCertificateDAO.findAllCertificatesByTagName(formatTagName(value), sortType, orderType);
            } else if (search.equals(SEARCH_BY_NAME) || search.equals(SEARCH_BY_DESCRIPTION)) {
                giftCertificateList = giftCertificateDAO.findAllCertificatesByNameOrDescription(value, sortType, orderType);
            } else {
                throw new RequestParamServiceException("Passed parameters don't match with allowable");
            }
        }
        return giftCertificateList.stream()
                .map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    private String formatTagName(String tagName) {
        return tagName.replace(UNDERSCORE, WHITESPACE);
    }

    private List<Tag> getListOutdatedTags(long idCertificate, List<Tag> tags) {
        List<Tag> currentTags = tagDAO.getTagsByGiftCertificateId(idCertificate);
        List<Tag> outdatedTags = new ArrayList<>();
        for (Tag tag : currentTags) {
            if (!tags.contains(tag)) {
                outdatedTags.add(tag);
            }
        }
        return outdatedTags;
    }

    private List<Tag> getListTagsNeedAttach(long idCertificate, List<Tag> tags) {
        List<Tag> currentTags = tagDAO.getTagsByGiftCertificateId(idCertificate);
        List<Tag> tagsNeedAttach = new ArrayList<>();
        for (Tag tag : tags) {
            if (!currentTags.contains(tag)) {
                tagsNeedAttach.add(tag);
            }
        }
        return tagsNeedAttach;
    }
}
