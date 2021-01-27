package com.epam.esm.controller;

import com.epam.esm.exceptionhandler.ValidationException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntryServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Class TagController is Rest controller for Tag
 */
@RestController
@RequestMapping("/api")
public class TagController {

    /**
     * service is used for operations with TagDto
     */
    @Autowired
    private TagService service;

    @Autowired
    private Validator tagDtoValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(tagDtoValidator);
    }

    /**
     * @return List<TagDto>
     * @method readAllTags reads all tags
     */
    @GetMapping("/tags")
    public List<TagDto> readAllTags() {
        return service.findAll();
    }

    /**
     * @param tagDto - it is new Tag
     * @return created tag as tegDto
     * @throws DuplicateEntryServiceException is such tag alredy exist in DB
     *                                        method createTag - create new tag in DB
     */
    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@Valid @RequestBody TagDto tagDto, BindingResult bindingResult) throws DuplicateEntryServiceException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("TagDto is not valid for create operation");
        }
        return service.create(tagDto);
    }

    /**
     * @param id
     * @return Tag with passed is as TagDto
     * @throws IdNotExistServiceException if Tag with such id doesn't exist in DB
     *                                    method readTagById - read tag by id
     */
    @GetMapping("/tags/{id}")
    public TagDto readTagById(@PathVariable long id) throws IdNotExistServiceException {
        return service.read(id);
    }

    /**
     * @param id
     * @throws IdNotExistServiceException if Tag with such id doesn't exist in DB
     * @method deleteTagById - delete tag by passed id
     */
    @DeleteMapping("/tags/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagById(@PathVariable long id) throws IdNotExistServiceException {
        service.delete(id);
    }

}
