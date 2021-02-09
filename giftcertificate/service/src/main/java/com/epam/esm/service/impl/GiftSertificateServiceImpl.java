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
import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Transactional(rollbackFor = DuplicateEntryServiceException.class)
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) throws DuplicateEntryServiceException {
        GiftCertificate createdGiftCertificate;
        long id;
        try {
         //  giftCertificateDto.setTags(getFilledTags(giftCertificateDto.getTags()));
            id = giftCertificateDAO.create(modelMapper.map(giftCertificateDto, GiftCertificate.class));
            createdGiftCertificate = giftCertificateDAO.read(id);
            giftCertificateDto = modelMapper.map(createdGiftCertificate, GiftCertificateDto.class);
        } catch (ConstraintViolationException e) {
            throw new DuplicateEntryServiceException("Gift certificate with same name alredy exist");
        }
        return giftCertificateDto;
    }

    /**
     * Read GiftCertificateDto from DB by id
     *
     * @param id id of GiftCertificate
     * @return GiftCertificateDto
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    @Transactional
    @Override
    public GiftCertificateDto read(long id) throws IdNotExistServiceException {
        GiftCertificate foundCertificate;
        GiftCertificateDto giftCertificateDto;
        List<TagDto> tagsDto;
        foundCertificate = giftCertificateDAO.read(id);
        if (foundCertificate == null) {
            throw new IdNotExistServiceException("There is no GiftCertificate with id = " + id + " in DB");
        }
        giftCertificateDto = modelMapper.map(foundCertificate, GiftCertificateDto.class);
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
    public void update(GiftCertificateDto modifiedGiftCertificateDto) throws IdNotExistServiceException,
            UpdateServiceException {
        GiftCertificate modifiedGiftCertificate = modelMapper.map(modifiedGiftCertificateDto, GiftCertificate.class);
        try {
            giftCertificateDAO.update(modifiedGiftCertificate);
        } catch (NonUniqueObjectException e) {
            throw new UpdateServiceException("GiftCertificatewith name = " + modifiedGiftCertificate.getName() + " alredy exist in DB");
        }
        logger.info("GiftCertificate has been updated in DB");
        //return read(modifiedGiftCertificateDto.getId());
    }

    /**
     * Delete GiftCertificate from DB by id
     *
     * @param id id of GiftCertificate
     * @throws IdNotExistServiceException if record with such id not exist in DB
     */
    @Override
    @Transactional(rollbackFor = IdNotExistServiceException.class)
    public void delete(long id) throws IdNotExistServiceException {
        GiftCertificate giftCertificate;
        giftCertificate = giftCertificateDAO.read(id);
        if (giftCertificate != null) {
            giftCertificateDAO.delete(giftCertificate);
        } else {
            throw new IdNotExistServiceException("There is no GiftCertificate with id = " + id + " in DB");
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
    @Transactional
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

    /*
    private List<TagDto> getFilledTags(List<TagDto> tagDtoList) {
        for (int i=0; i<tagDtoList.size();i++) {
            Tag tag = tagDAO.getTagByName(tagDtoList.get(i).getName());
            if (tag != null) {
                tagDtoList.set(i, modelMapper.map(tag, TagDto.class));
            }
        }
        return tagDtoList;
    }


     */
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

}
