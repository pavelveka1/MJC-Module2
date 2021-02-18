package com.epam.esm.controller;

import com.epam.esm.exceptionhandler.ValidationException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.*;
import com.epam.esm.validator.GiftCertificateDtoPatchValidator;
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
@RequestMapping("/api/certificates")
public class GiftCertificateController {

    private static final String DEFAULT_PAGE_SIZE = "1000";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private static final Logger logger = Logger.getLogger(GiftCertificateController.class);
    private static final String CERTIFICATE_DTO_NOT_VALID = "GiftCertificateDto_not_valid";
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
    @GetMapping
    public List<GiftCertificateDto> readAll(@RequestParam(required = false) String search,
                                            @RequestParam(required = false) String[] values,
                                            @RequestParam(required = false) String sortType,
                                            @RequestParam(required = false) String orderType,
                                            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer size)
            throws RequestParamServiceException, IdNotExistServiceException, PaginationException {
        logger.info("read all giftCertificates");
        List<GiftCertificateDto> giftCertificateDtoList = service.findAll(search, values, sortType, orderType, page, size);
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
    @GetMapping("/{id}")
    public GiftCertificateDto read(@PathVariable long id) throws IdNotExistServiceException {
        GiftCertificateDto giftCertificateDto = service.read(id);
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@Valid @RequestBody GiftCertificateDto giftCertificateDto, BindingResult bindingResult)
            throws DuplicateEntryServiceException, ValidationException, IdNotExistServiceException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(CERTIFICATE_DTO_NOT_VALID);
        }
        GiftCertificateDto giftCertificateDtoResult = service.create(giftCertificateDto);
        HATEOASBuilder.addLinksToGiftCertificate(giftCertificateDtoResult);
        return giftCertificateDtoResult;
    }

    /**
     * Method let to update GiftCertificate partly
     *
     * @param id                 of giftCertificate
     * @param giftCertificateDto passed as json
     * @return GiftCertificateDto
     * @throws IdNotExistServiceException     if  certificate with such id not exist
     * @throws ValidationException            if passed GiftCertificateDto is not valid
     * @throws DuplicateEntryServiceException if was attempt to rename certificate with alrery existing name
     */
    @PatchMapping("/{id}")
    public GiftCertificateDto updateGiftCertificate(@PathVariable("id") long id,
                                                    @RequestBody GiftCertificateDto giftCertificateDto)
            throws IdNotExistServiceException, ValidationException, DuplicateEntryServiceException {

        if (!GiftCertificateDtoPatchValidator.validate(giftCertificateDto)) {
            throw new ValidationException(CERTIFICATE_DTO_NOT_VALID);
        }
        giftCertificateDto.setId(id);
        service.update(giftCertificateDto);
        GiftCertificateDto giftCertificateDtoResult = service.read(id);
        HATEOASBuilder.addLinksToGiftCertificate(giftCertificateDtoResult);
        return giftCertificateDtoResult;
    }

    /**
     * Method delete - delete GiftCertificate from DB by id
     *
     * @param id of GiftCertificate
     * @throws IdNotExistServiceException if GiftCertificate with such id doesn't exist in DB
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id)
            throws IdNotExistServiceException {
        service.delete(id);
    }
}