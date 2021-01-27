package com.epam.esm.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.*;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @class GiftCertificateService
 * Contains methods for work with GiftCertificateDto
 */
@Service
@Data
public class GiftSertificateServiceImpl implements GiftCertificateService {

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
     * @param modelMapper                 for convertion object
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
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) throws DuplicateEntryServiceException, TagNameNotExistServiceException {
        GiftCertificate createdGiftCertificate;

        try {
            createAndSetTags(giftCertificateDto);
            createdGiftCertificate = giftCertificateDAO.create(modelMapper.map(giftCertificateDto, GiftCertificate.class));
            return modelMapper.map(createdGiftCertificate, GiftCertificateDto.class);
        } catch (DuplicateEntryDAOException e) {
            throw new DuplicateEntryServiceException(e.getMessage());
        } catch (TagNameNotExistDAOException ex) {
            throw new TagNameNotExistServiceException(ex.getMessage());
        }

    }

    /**
     * Read GiftCertificateDto from DB by id
     *
     * @param id id of GiftCertificate
     * @return GiftCertificateDto
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    @Override
    @Transactional
    public GiftCertificateDto read(long id) throws IdNotExistServiceException {
        GiftCertificate foundCertificate;
        try {
            foundCertificate = giftCertificateDAO.read(id);
        } catch (IdNotExistDAOException e) {
            throw new IdNotExistServiceException(e.getMessage());
        }
        return modelMapper.map(foundCertificate, GiftCertificateDto.class);
    }

    /**
     * Update GiftCertificate as GiftCertificateDto
     *
     * @param modifiedGiftCertificateDto modified GiftCertificate
     * @return updated GiftCertificateDto
     */
    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto modifiedGiftCertificateDto) throws IdNotExistServiceException, UpdateServiceException {
        GiftCertificate updateGiftCertificate;
        GiftCertificate modifiedGiftCertificate = modelMapper.map(modifiedGiftCertificateDto, GiftCertificate.class);
        GiftCertificateDto readGiftCertificateDto = read(modifiedGiftCertificateDto.getId());
        GiftCertificate readGiftCertificate = modelMapper.map(readGiftCertificateDto, GiftCertificate.class);
        updateGiftCertificateFields(readGiftCertificate, modifiedGiftCertificate);
        try {
            updateGiftCertificate = giftCertificateDAO.update(readGiftCertificate);
        } catch (UpdateDAOException e) {
            throw new UpdateServiceException(e.getMessage());
        }
        return modelMapper.map(updateGiftCertificate, GiftCertificateDto.class);
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
        try {
            giftCertificateDAO.delete(id);
        } catch (IdNotExistDAOException e) {
            throw new IdNotExistServiceException(e.getMessage());
        }
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
    public List<GiftCertificateDto> findAll(String sortType, String orderType) throws RequestParamServiceException {
        List<GiftCertificate> giftCertificates;
        try {
            giftCertificates = giftCertificateDAO.findAll(sortType, orderType);
        } catch (RequestParamDAOException e) {
            throw new RequestParamServiceException(e.getMessage());
        }
        return giftCertificates.stream()
                .map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }


    /**
     * Method creates tags in DB if such tags aren't exist yet
     *
     * @param giftCertificateDto it contains data of GiftCertificate
     * @throws TagNameNotExistDAOException
     */
    private void createAndSetTags(GiftCertificateDto giftCertificateDto) throws TagNameNotExistDAOException {
        List<TagDto> tags = new ArrayList<>();
        if (giftCertificateDto.getTags() != null) {
            TagDto add;
            for (TagDto tagDto : giftCertificateDto.getTags()) {
                Tag tag = modelMapper.map(tagDto, Tag.class);
                try {
                    tag = tagDAO.create(tag);
                    add = modelMapper.map(tag, TagDto.class);
                    tags.add(add);
                } catch (DuplicateEntryDAOException tagExist) {
                    add = modelMapper.map(tagDAO.readTagByName(tag.getName()), TagDto.class);
                    tags.add(add);
                }
            }
            giftCertificateDto.setTags(tags);
        }
    }

    /**
     * Method updates fieflds GiftCertificate
     *
     * @param readGiftCertificateDto  original GiftCertificate
     * @param modifiedGiftCertificate updated GiftCertificate
     */
    private void updateGiftCertificateFields(GiftCertificate readGiftCertificateDto, GiftCertificate
            modifiedGiftCertificate) {
        if (modifiedGiftCertificate.getDuration() != null) {
            readGiftCertificateDto.setDuration(modifiedGiftCertificate.getDuration());
        }
        if (modifiedGiftCertificate.getDescription() != null) {
            readGiftCertificateDto.setDescription(modifiedGiftCertificate.getDescription());
        }
        if (modifiedGiftCertificate.getName() != null) {
            readGiftCertificateDto.setName(modifiedGiftCertificate.getName());
        }
        if (modifiedGiftCertificate.getPrice() != null) {
            readGiftCertificateDto.setPrice(modifiedGiftCertificate.getPrice());
        }
        readGiftCertificateDto.setLastUpdateDate(LocalDateTime.now(ZoneOffset.systemDefault()));
    }
}
