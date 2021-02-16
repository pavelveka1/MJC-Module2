package com.epam.esm.controller;

import com.epam.esm.exceptionhandler.ValidationException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntryServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Class TagController is Rest controller for Tag
 */
@RestController
@RequestMapping("/api")
public class TagController {

    private static final String DEFAULT_PAGE_SIZE = "1000";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    /**
     * service is used for operations with TagDto
     */
    @Autowired
    private TagService service;

    /**
     * tagDtoValidator is used  for validation of TagDto
     */
    @Autowired
    private Validator tagDtoValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(tagDtoValidator);
    }

    /**
     * Method readAllTags reads all tags
     *
     * @param page number of page
     * @param size number of entity on page
     * @return List of TagDto
     * @throws IdNotExistServiceException can be thrown by HATEOASBuilder while reading by id
     * @throws PaginationException        if page number equals zero
     */
    @GetMapping("/tags")
    public List<TagDto> readAllTags(@RequestParam( defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                    @RequestParam( defaultValue = DEFAULT_PAGE_SIZE) Integer size)
            throws IdNotExistServiceException, PaginationException {
        List<TagDto> tagDtoList = service.findAll(page, size);
        HATEOASBuilder.addLinksToTags(tagDtoList);
        return tagDtoList;
    }

    /**
     * Method createTag - create new tag in DB
     *
     * @param tagDto - it is new Tag
     * @return created tag as tegDto
     * @throws DuplicateEntryServiceException is such tag alredy exist in DB
     * @throws ValidationException            if passed TagDto is not valid
     */
    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@Valid @RequestBody TagDto tagDto, BindingResult bindingResult)
            throws DuplicateEntryServiceException, ValidationException, IdNotExistServiceException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("TagDto is not valid for create operation");
        }
        TagDto tagDtoResult = service.create(tagDto);
        HATEOASBuilder.addLinksToTag(tagDtoResult);
        return tagDtoResult;
    }

    /**
     * Method readTagById - read tag by id
     *
     * @param id - id of Tag which will be read
     * @return TagDto with passed is as TagDto
     * @throws IdNotExistServiceException if Tag with such id doesn't exist in DB
     */
    @GetMapping("/tags/{id}")
    public TagDto readTagById(@PathVariable long id) throws IdNotExistServiceException {
        TagDto tagDto = service.read(id);
        HATEOASBuilder.addLinksToTag(tagDto);
        return tagDto;
    }

    /**
     * Method deleteTagById - delete tag by passed id
     *
     * @param id id of Tag which will be deleted
     * @throws IdNotExistServiceException if Tag with such id doesn't exist in DB
     */
    @DeleteMapping("/tags/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagById(@PathVariable long id) throws IdNotExistServiceException {
        service.delete(id);
    }

    /**
     * Method gets widely used by user tag with max sum of cost in orders
     *
     * @param id id of user
     * @return TagDto
     * @throws IdNotExistServiceException if user with such id is not exist in DB
     */
    @GetMapping("/tags/top_tag/users/{id}")
    public TagDto readWidelyUsedTagByUserWithMaxCost(@PathVariable long id) throws IdNotExistServiceException {
        TagDto tagDto = service.getWidelyUsedByUserTagWithHighestCost(id);
        HATEOASBuilder.addLinksToTag(tagDto);
        return tagDto;
    }
}
