package com.epam.esm.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntryServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class TagServiceImpl.
 * Contains methods for work with Tag class
 */
@Service
public class TagServiceImpl implements TagService {

    private static final String KEY_TAG_DUPLICATE = "tag_duplicate";
    private static final String KEY_TAG_ID_NOT_FOUND = "tag_id_not_found";
    private static final String KEY_USER_NOT_FOUND_OR_NOR_ORDERS = "user_not_found_or_not_orders";
    private static final String KEY_PAGINATION = "pagination";
    private static final int ZERO = 0;
    private static final int ONE = 1;
    /**
     * TagDAO is used for operations with Tag
     */
    @Autowired
    private TagDAO tagDAO;

    /**
     * GiftCertificateDAO is used for operations with GiftCertificate
     */
    @Autowired
    private GiftCertificateDAO giftCertificateDAO;

    /**
     * ModelMapper is used for convertation TagDto to Tag
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Empty constructor
     */
    public TagServiceImpl() {

    }

    /**
     * Constcuctor with all parameters
     *
     * @param tagDAO      for operations with Tag
     * @param modelMapper for convertion object
     */
    public TagServiceImpl(TagDAO tagDAO, ModelMapper modelMapper) {
        this.tagDAO = tagDAO;
        this.modelMapper = modelMapper;
    }

    /**
     * Create new tag in DB
     *
     * @param tagDto it contains data of Tag will be created
     * @return created TagDto
     * @throws DuplicateEntryServiceException if this Tag already exists in the DB
     */
    @Transactional(rollbackFor = DuplicateEntryServiceException.class)
    @Override
    public TagDto create(TagDto tagDto, String language) throws DuplicateEntryServiceException {
        Tag addedTag;
        long id;
        try {
            id = tagDAO.create(modelMapper.map(tagDto, Tag.class));
            addedTag = tagDAO.read(id);
        } catch (ConstraintViolationException e) {
            throw new DuplicateEntryServiceException(KEY_TAG_DUPLICATE, language);
        }
        return modelMapper.map(addedTag, TagDto.class);
    }

    /**
     * Read one Tag from DB by id
     *
     * @param id id of Tag
     * @return Optional<Tag>
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    @Transactional
    @Override
    public TagDto read(long id, String language) throws IdNotExistServiceException {
        Tag readTag;
        TagDto tagDto;
        try {
            readTag = tagDAO.read(id);
            tagDto = modelMapper.map(readTag, TagDto.class);
        } catch (IllegalArgumentException e) {
            throw new IdNotExistServiceException(KEY_TAG_ID_NOT_FOUND, language);
        }

        return tagDto;
    }

    /**
     * Delete Tag from DB by id
     *
     * @param id id of Tag
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    @Transactional
    @Override
    public void delete(long id, String language) throws IdNotExistServiceException {
        Tag tag = tagDAO.read(id);
        if (Objects.isNull(tag)) {
            throw new IdNotExistServiceException(KEY_TAG_ID_NOT_FOUND, language);
        }
        tagDAO.delete(tag);
    }


    /**
     * Find all Tags
     *
     * @return list of TagDto
     */
    @Transactional
    @Override
    public List<TagDto> findAll(Integer page, Integer size, String language) throws PaginationException {
        page = checkPage(page, language);
        size = checkSizePage(size);
        List<Tag> tags = tagDAO.findAll(page, size);
        return tags.stream().map(tag -> modelMapper.map(tag, TagDto.class)).collect(Collectors.toList());
    }

    @Override
    public TagDto getWidelyUsedByUserTagWithHighestCost(long userId, String language) throws IdNotExistServiceException {
        long idTag;
        try {
            idTag = tagDAO.getIdWidelyUsedByUserTagWithHighestCost(userId);
        } catch (NullPointerException e) {
            throw new IdNotExistServiceException(KEY_USER_NOT_FOUND_OR_NOR_ORDERS, language);
        }
        TagDto tagDto = modelMapper.map(tagDAO.read(idTag), TagDto.class);
        return tagDto;
    }

    private Integer checkPage(Integer page, String language) throws PaginationException {
        if (page < ONE) {
            if (page == ZERO) {
                throw new PaginationException(KEY_PAGINATION, language);
            }
            page = Math.abs(page);
        }
        return page;
    }

    private Integer checkSizePage(Integer size) throws PaginationException {
        if (size < ONE) {
            size = Math.abs(size);
        }
        return size;
    }
}
