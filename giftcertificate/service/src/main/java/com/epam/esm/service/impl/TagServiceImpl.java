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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagServiceImpl implements TagService {

    private static final String KEY_TAG_DUPLICATE = "tag_duplicate";
    private static final String KEY_TAG_ID_NOT_FOUND = "tag_id_not_found";
    private static final String KEY_USER_NOT_FOUND_OR_NOR_ORDERS = "user_not_found_or_not_orders";
    private static final String ID = "id";

    @Autowired
    private TagDAO tagDAO;

    @Autowired
    private GiftCertificateDAO giftCertificateDAO;

    @Autowired
    private ModelMapper modelMapper;

    public TagServiceImpl() {

    }

    public TagServiceImpl(TagDAO tagDAO, ModelMapper modelMapper) {
        this.tagDAO = tagDAO;
        this.modelMapper = modelMapper;
    }

    @Transactional(rollbackFor = DuplicateEntryServiceException.class)
    @Secured("ROLE_ADMIN")
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


    @Transactional
    @Secured("ROLE_ADMIN")
    @Override
    public void delete(long id) throws IdNotExistServiceException {
        Optional<Tag> tag = tagDAO.findById(id);
        if (!tag.isPresent()) {
            throw new IdNotExistServiceException(KEY_TAG_ID_NOT_FOUND);
        }
        tagDAO.delete(tag.get());
    }

    @Override
    public List<TagDto> findAll(Integer page, Integer size) throws PaginationException {
        page = PaginationUtil.checkPage(page);
        size = PaginationUtil.checkSizePage(size);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(ID).ascending());
        List<Tag> tags = tagDAO.findAll(pageable).getContent();
        return tags.stream().map(tag -> modelMapper.map(tag, TagDto.class)).collect(Collectors.toList());
    }

    @Override
    @Secured("ROLE_ADMIN")
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
