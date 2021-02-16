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
import java.util.List;

/**
 * Class GiftCertificateController - Rest controller for process of request to GiftCertificates
 */
@RestController
@RequestMapping("/controller/api")
public class GiftCertificateController {

    private static final String DEFAULT_PAGE_SIZE = "1000";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private static final Logger logger = Logger.getLogger(GiftCertificateController.class);
    private static final String CERTIFICATE_DTO_NOT_VALID = "GiftCertificateDto_not_valid";
    private static final String LOCALE_EN = "en";
    /**
     * GiftCertificateService is used for work with GiftCertificateDto
     */
    @Autowired
    private GiftCertificateService service;

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
     * Method readAll - reads all GiftCertificates from DB
     *
     * @param search    type of search
     * @param values    name of value
     * @param sortType  it is name of field in table gitf_certificates of DB
     * @param orderType ASC or DESC
     * @param page      number of page
     * @param size      number of entity on page
     * @return List of GiftCertificateDto
     * @throws RequestParamServiceException if params don't correlate with name of field in DB
     * @throws IdNotExistServiceException   can be thrown by HATEOASBuilder while reading by id
     * @throws PaginationException          if page number equals zero
     */
    @GetMapping("/certificates")
    public List<GiftCertificateDto> readAll(@RequestParam(required = false) String search,
                                            @RequestParam(required = false) String[] values,
                                            @RequestParam(required = false) String sortType,
                                            @RequestParam(required = false) String orderType,
                                            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                            @RequestParam(required = false, defaultValue = LOCALE_EN) String language)
            throws RequestParamServiceException, IdNotExistServiceException, PaginationException {
        logger.info("read all giftCertificates");
        List<GiftCertificateDto> giftCertificateDtoList = service.findAll(search, values, sortType, orderType, page, size, language);
        HATEOASBuilder.addLinksToGiftCertificates(giftCertificateDtoList);
        return giftCertificateDtoList;
    }

    /**
     * method read - read one GiftCertificate from DB by passed id
     *
     * @param id of GiftCertificate
     * @return GiftCertificateDto
     * @throws IdNotExistServiceException if GiftCertificate with such id doesn't exist in DB
     */
    @GetMapping("/certificates/{id}")
    public GiftCertificateDto read(@PathVariable long id, @RequestParam(required = false, defaultValue = LOCALE_EN)
            String language) throws IdNotExistServiceException {
        GiftCertificateDto giftCertificateDto = service.read(id, language);
        HATEOASBuilder.addLinksToGiftCertificate(giftCertificateDto);
        return giftCertificateDto;
    }

    /**
     * Method create - creates new GiftCertificate in DB
     *
     * @param giftCertificateDto contains data for creation of GiftCertificate
     * @return created GiftCertificate as GiftCertificateDto
     * @throws DuplicateEntryServiceException if such giftCertificate alredy exist in DB
     * @throws ValidationException            if passed GiftCertificateDto is not valid
     */
    @PostMapping("/certificates")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@Valid @RequestBody GiftCertificateDto giftCertificateDto, BindingResult bindingResult,
                                     @RequestParam(required = false, defaultValue = LOCALE_EN) String language)
            throws DuplicateEntryServiceException,
            ValidationException, IdNotExistServiceException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(CERTIFICATE_DTO_NOT_VALID, language);
        }
        GiftCertificateDto giftCertificateDtoResult = service.create(giftCertificateDto, language);
        HATEOASBuilder.addLinksToGiftCertificate(giftCertificateDtoResult);
        return giftCertificateDtoResult;
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
                                                    @RequestBody GiftCertificateDto giftCertificateDto,
                                                    @RequestParam(required = false, defaultValue = LOCALE_EN) String language,
                                                    BindingResult bindingResult)
            throws IdNotExistServiceException, UpdateServiceException, ValidationException, DuplicateEntryServiceException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("GiftCertificateDto has fields, that is not valid for update operation!");
        }
        giftCertificateDto.setId(id);
        service.update(giftCertificateDto, language);
        GiftCertificateDto giftCertificateDtoResult = service.read(id, language);
        HATEOASBuilder.addLinksToGiftCertificate(giftCertificateDtoResult);
        return giftCertificateDtoResult;
    }

    /**
     * Method delete - delete GiftCertificate from DB by id
     *
     * @param id of GiftCertificate
     * @throws IdNotExistServiceException if GiftCertificate with such id doesn't exist in DB
     */
    @DeleteMapping("/certificates/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @RequestParam(required = false, defaultValue = LOCALE_EN) String language)
            throws IdNotExistServiceException {
        service.delete(id, language);
    }

}