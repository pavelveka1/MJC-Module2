package com.epam.esm.service;

import java.util.List;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntryServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;

/**
 * Interface TagService.
 * Contains methods for work with Tag class
 */
public interface TagService {

    /**
     * Create new tag in DB
     *
     * @param tagDto it contains data of Tag will be created
     * @return created TagDto
     * @throws DuplicateEntryServiceException if this Tag already exists in the DB
     */
    TagDto create(TagDto tagDto) throws DuplicateEntryServiceException;

    /**
     * Read one Tag from DB by id
     *
     * @param id id of Tag
     * @return Optional<Tag>
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    TagDto read(long id) throws IdNotExistServiceException;

    /**
     * Delete Tag from DB by id
     *
     * @param id id of Tag
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    void delete(long id) throws IdNotExistServiceException;

    /**
     * Find all Tags
     *
     * @return list of TagDto
     */
    List<TagDto> findAll();
}
