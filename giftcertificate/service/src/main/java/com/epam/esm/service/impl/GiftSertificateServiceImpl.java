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
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String DEFAULT_SEARCH_TYPE = "name";
    private static final String DEFAULT_VALUES = "";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAG = "tag";
    private static final int ZERO = 0;
    private static final String KEY_NO_SUCH_FIELD = "certificate_no_such_field";
    private static final String KEY_DUPLICATE_CERTIFICATE = "certificate_duplicate";
    private static final String KEY_ID_NOT_EXIST = "certificate_no_id";
    private static final String KEY_NO_PASSED_VALUE = "certificate_no_value";
    private static final String KEY_SEARCH_VALUE_INVALID = "certificate_search_param";

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
        GiftCertificate createdGiftCertificate;
        giftCertificateDto.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()));
        giftCertificateDto.setLastUpdateDate(giftCertificateDto.getCreateDate());
        long id;
        try {
            id = giftCertificateDAO.create(modelMapper.map(giftCertificateDto, GiftCertificate.class));
            createdGiftCertificate = giftCertificateDAO.read(id);
            giftCertificateDto = modelMapper.map(createdGiftCertificate, GiftCertificateDto.class);
        } catch (ConstraintViolationException e) {
            throw new DuplicateEntryServiceException(KEY_DUPLICATE_CERTIFICATE);
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
    @Override
    public GiftCertificateDto read(long id) throws IdNotExistServiceException {
        GiftCertificate foundCertificate;
        GiftCertificateDto giftCertificateDto;
        foundCertificate = giftCertificateDAO.read(id);
        if (foundCertificate == null) {
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
        GiftCertificate giftCertificateRead = giftCertificateDAO.read(modifiedGiftCertificateDto.getId());
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
        giftCertificateDAO.update(giftCertificateRead);
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
        giftCertificate = giftCertificateDAO.read(id);
        if (Objects.nonNull(giftCertificate)) {
            giftCertificateDAO.delete(giftCertificate);
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
    public List<GiftCertificateDto> findAll(String search, String[] values, String sortType, String orderType,
                                            Integer page, Integer size)
            throws PaginationException, RequestParamServiceException {
        List<GiftCertificate> giftCertificates;
        List<GiftCertificateDto> giftCertificateDtoList;
        String nameOrDescription = null;
        List<Tag> tags = new ArrayList<>();
        page = PaginationUtil.checkPage(page);
        size = PaginationUtil.checkSizePage(size);
        if (StringUtils.isEmpty(sortType)) {
            sortType = DEFAULT_SORT_TYPE;
        }
        if (StringUtils.isEmpty(orderType)) {
            orderType = DEFAULT_SORT_ORDER;
        }
        if (StringUtils.isEmpty(search)) {
            search = DEFAULT_SEARCH_TYPE;
            nameOrDescription = DEFAULT_VALUES;
        } else if (search.equals(NAME) || search.equals(DESCRIPTION)) {
            if (Objects.isNull(values)) {
                throw new RequestParamServiceException(KEY_NO_PASSED_VALUE);
            } else {
                nameOrDescription = values[ZERO];
            }
        } else if (search.equals(TAG)) {
            tags = getListTagsByNames(values);
        } else {
            throw new RequestParamServiceException(KEY_SEARCH_VALUE_INVALID);
        }
        try {
            giftCertificates = giftCertificateDAO.findAll(search, tags, nameOrDescription, sortType, orderType, page, size);
        } catch (IllegalArgumentException e) {
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
                if ((Objects.nonNull(tag)) && (!tags.contains(tag))) {
                    tags.add(tag);
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
