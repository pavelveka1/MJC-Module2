package com.epam.esm.service.impl;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntryServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class TagServiceImpl.
 * Contains methods for work with Tag class
 */
@Service
public class TagServiceImpl implements TagService {

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
        try{
            id = tagDAO.create(modelMapper.map(tagDto, Tag.class));
            addedTag = tagDAO.read(id);
        }catch (ConstraintViolationException e){
            throw new DuplicateEntryServiceException("Tag with name = "+tagDto.getName()+" alredy exist in DB");
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
    public TagDto read(long id) throws IdNotExistServiceException {
        Tag readTag;
        TagDto tagDto;
        try {
            readTag = tagDAO.read(id);
            tagDto = modelMapper.map(readTag, TagDto.class);
        } catch (IllegalArgumentException e) {
            throw new IdNotExistServiceException("Tag with id = " + id + " not found");
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
        Tag tag=tagDAO.read(id);
        try{
            tagDAO.delete(tag);
        }catch (IllegalArgumentException e){
            throw new IdNotExistServiceException("There is no Tag with id = " + id + " in DB");
        }

    }


    /**
     * Find all Tags
     *
     * @return list of TagDto
     */
    @Transactional
    @Override
    public List<TagDto> findAll() {
        List<Tag> tags = tagDAO.findAll();
        return tags.stream().map(tag -> modelMapper.map(tag, TagDto.class)).collect(Collectors.toList());
    }

}
