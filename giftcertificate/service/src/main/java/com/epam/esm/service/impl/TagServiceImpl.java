package com.epam.esm.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntryServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import com.epam.esm.service.util.PaginationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private static final String ID = "id";

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
    public TagDto create(TagDto tagDto) throws DuplicateEntryServiceException {
        Tag addedTag;
        long id;
        addedTag = modelMapper.map(tagDto, Tag.class);
        try {
            addedTag = tagDAO.save(addedTag);
        } catch (Exception e) {
            throw new DuplicateEntryServiceException(KEY_TAG_DUPLICATE);
        }
        return modelMapper.map(addedTag, TagDto.class);
    }

    /**
     * Read one Tag from DB by id
     *
     * @param id id of Tag
     * @return TagDto
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    @Override
    public TagDto read(long id) throws IdNotExistServiceException {
        Optional<Tag> readTag;
        Tag tag;
        TagDto tagDto;
        try {
            readTag = tagDAO.findById(id);
            if (readTag.isPresent()) {
                tag = readTag.get();
            } else {
                throw new IdNotExistServiceException(KEY_TAG_ID_NOT_FOUND);
            }
            tagDto = modelMapper.map(tag, TagDto.class);
        } catch (IllegalArgumentException e) {
            throw new IdNotExistServiceException(KEY_TAG_ID_NOT_FOUND);
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
    public void delete(long id) throws IdNotExistServiceException {
        Optional<Tag> tag = tagDAO.findById(id);
        if (!tag.isPresent()) {
            throw new IdNotExistServiceException(KEY_TAG_ID_NOT_FOUND);
        }
        tagDAO.delete(tag.get());
    }


    /**
     * Find all Tags
     *
     * @return list of TagDto
     */
    @Override
    public List<TagDto> findAll(Integer page, Integer size) throws PaginationException {
        page = PaginationUtil.checkPage(page);
        size = PaginationUtil.checkSizePage(size);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(ID).ascending());
        List<Tag> tags = tagDAO.findAll(pageable).getContent();
        return tags.stream().map(tag -> modelMapper.map(tag, TagDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TagDto getWidelyUsedByUserTagWithHighestCost(long userId) throws IdNotExistServiceException {
        long idTag;
        try {
            idTag = tagDAO.getIdWidelyUsedByUserTagWithHighestCost(userId);
        } catch (NullPointerException e) {
            throw new IdNotExistServiceException(KEY_USER_NOT_FOUND_OR_NOR_ORDERS);
        }
        return modelMapper.map(tagDAO.findById(idTag).get(), TagDto.class);
    }
}
