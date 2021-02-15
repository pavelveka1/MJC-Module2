package com.epam.esm.service;

import java.util.List;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntryServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;

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
    TagDto create(TagDto tagDto, String language) throws DuplicateEntryServiceException;

    /**
     * Read one Tag from DB by id
     *
     * @param id id of Tag
     * @return Optional<Tag>
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    TagDto read(long id, String language) throws IdNotExistServiceException;

    /**
     * Delete Tag from DB by id
     *
     * @param id id of Tag
     * @throws IdNotExistServiceException if records with such id not exist in DB
     */
    void delete(long id, String language) throws IdNotExistServiceException;

    /**
     * Find all Tags
     *
     * @return list of TagDto
     * @throws PaginationException if page equals zero
     */
    List<TagDto> findAll(Integer page, Integer size, String language) throws PaginationException;

    /**
     * Get widely used by user tag with max sun cost
     *
     * @param userId id of user
     * @return TagDto
     * @throws IdNotExistServiceException if user with passed is not exist in DB
     */
    TagDto getWidelyUsedByUserTagWithHighestCost(long userId, String language) throws IdNotExistServiceException;
}
