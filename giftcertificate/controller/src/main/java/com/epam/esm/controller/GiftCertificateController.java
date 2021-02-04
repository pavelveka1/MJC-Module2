package com.epam.esm.controller;

import com.epam.esm.exceptionhandler.ValidationException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Class GiftCertificateController - Rest controller for process of request to GiftCertificates
 */
@RestController
@RequestMapping("/api")
public class GiftCertificateController {

    private static final Logger logger = Logger.getLogger(GiftCertificateController.class);
    /**
     * GiftCertificateService is used for work with GiftCertificateDto
     */
    @Autowired
    private GiftCertificateService service;

    @Autowired
    private GiftCertificateService service1;
    /**
     * giftCertificateDtoValidator is used  for validation of GiftCertificateDto
     */
    @Autowired
    private Validator giftCertificateDtoValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(giftCertificateDtoValidator);
    }

    /**
     * method readAll - reads all GiftCertificates from DB
     *
     * @param sortType  - it is name of field in table gitf_certificates of DB
     * @param orderType ASC or DESC
     * @return List<GiftCertificateDto>
     * @throws RequestParamServiceException if params don't correlate with name of field in DB
     */
    @GetMapping("/certificates")
    public List<GiftCertificateDto> readAll(@RequestParam(required = false) String search, @RequestParam(required = false) String value, @RequestParam(required = false) String sortType, @RequestParam(required = false) String orderType) throws RequestParamServiceException {
        logger.info("read all giftCertificates");
        return service.findAll(search, value, sortType, orderType);
    }

    /**
     * method read - read one GiftCertificate from DB by passed id
     *
     * @param id of GiftCertificate
     * @return GiftCertificateDto
     * @throws IdNotExistServiceException if GiftCertificate with such id doesn't exist in DB
     */
    @GetMapping("/certificates/{id}")
    public GiftCertificateDto read(@PathVariable @Min(5) int id) throws IdNotExistServiceException {
        return service.read(id);
    }

    /**
     * Method create - creates new GiftCertificate in DB
     *
     * @param giftCertificateDto contains data for creation of GiftCertificate
     * @return created GiftCertificate as GiftCertificateDto
     * @throws DuplicateEntryServiceException  if such giftCertificate alredy exist in DB
     * @throws TagNameNotExistServiceException if Tag with such name is not found
     * @throws ValidationException             if passed GiftCertificateDto is not valid
     */
    @PostMapping("/certificates")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@Valid @RequestBody GiftCertificateDto giftCertificateDto, BindingResult bindingResult) throws DuplicateEntryServiceException, ValidationException, TagNotExistServiceException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("GiftCertificateDto is not valid for create operation");
        }
        return service.create(giftCertificateDto);
    }

    /**
     * Method update - updates GiftCertificate
     *
     * @param giftCertificateDto modified
     * @return updated GiftCertificate as GiftCertificateDto
     * @throws IdNotExistServiceException if GiftCertificate with such id doesn't exist in DB
     * @throws UpdateServiceException     if giftCertificate hasn't been updated
     * @throws ValidationException        if passed GiftCertificateDto is not valid
     */
    @PutMapping("/certificates")
    public GiftCertificateDto update(@Valid @RequestBody GiftCertificateDto giftCertificateDto, BindingResult bindingResult) throws IdNotExistServiceException, ValidationException, UpdateServiceException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("GiftCertificateDto is not valid for update operation!");
        }
        return service.update(giftCertificateDto);
    }


    /**
     * Method let to update GiftCertificate partly
     *
     * @param id                 of giftCertificate
     * @param giftCertificateDto passed as json
     * @param bindingResult      result of validation
     * @return GiftCertificateDto
     * @throws IdNotExistServiceException if  certificate with such id not exist
     * @throws UpdateServiceException     if giftCertificate hasn't been updated
     * @throws ValidationException        if passed GiftCertificateDto is not valid
     */
    @PatchMapping("/certificates/{id}")
    public GiftCertificateDto updateGiftCertificate(@PathVariable("id") long id,
                                                    @RequestBody GiftCertificateDto giftCertificateDto, BindingResult bindingResult) throws IdNotExistServiceException, UpdateServiceException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("GiftCertificateDto has fields, that is not valid for update operation!");
        }
        GiftCertificateDto giftCertificateDtoRead = service.read(id);

        if (giftCertificateDto.getName() != null) {

            giftCertificateDtoRead.setName(giftCertificateDto.getName());
        }
        if (giftCertificateDto.getDescription() != null) {
            giftCertificateDtoRead.setDescription(giftCertificateDto.getDescription());
        }
        if (giftCertificateDto.getPrice() != null) {
            giftCertificateDtoRead.setPrice(giftCertificateDto.getPrice());
        }
        if (giftCertificateDto.getDuration() != null) {
            giftCertificateDtoRead.setDuration(giftCertificateDto.getDuration());
        }

        service.update(giftCertificateDtoRead);

        return service.read(id);
    }

    /**
     * method delete - delete GiftCertificate from DB by id
     *
     * @param id of GiftCertificate
     * @throws IdNotExistServiceException if GiftCertificate with such id doesn't exist in DB
     */
    @DeleteMapping("/certificates/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) throws IdNotExistServiceException {
        service.delete(id);
    }
}