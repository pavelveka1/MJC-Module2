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
 * Class TagController is Rest controller for Tag
 */
@RestController
@RequestMapping("/api")
public class TagController {

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
     * method readAllTags reads all tags
     *
     * @return List<TagDto>
     */
    @GetMapping("/tags")
    public List<TagDto> readAllTags() {
        return service.findAll();
    }

    /**
     * Method createTag - create new tag in DB
     *
     * @param tagDto - it is new Tag
     * @return created tag as tegDto
     * @throws DuplicateEntryServiceException is such tag alredy exist in DB
     * @throws ValidationException if passed TagDto is not valid
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
     * method readTagById - read tag by id
     *
     * @param id - id of Tag which will be read
     * @return Tag with passed is as TagDto
     * @throws IdNotExistServiceException if Tag with such id doesn't exist in DB
     */
    @GetMapping("/tags/{id}")
    public TagDto readTagById(@PathVariable long id) throws IdNotExistServiceException {
        return service.read(id);
    }

    /**
     * method deleteTagById - delete tag by passed id
     *
     * @param id id of Tag which will be deleted
     * @throws IdNotExistServiceException if Tag with such id doesn't exist in DB
     */
    @DeleteMapping("/tags/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagById(@PathVariable long id) throws IdNotExistServiceException {
        service.delete(id);
    }

}
