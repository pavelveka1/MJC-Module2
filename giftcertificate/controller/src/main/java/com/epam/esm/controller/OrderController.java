package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.exceptionhandler.ValidationException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.CertificateNameNotExistServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import com.epam.esm.validator.OrderValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/controller/api")
public class OrderController {

    private static final Logger logger = Logger.getLogger(GiftCertificateController.class);
    private static final String DEFAULT_PAGE_SIZE = "1000";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private static final String LOCALE_EN = "en";
    /**
     * OrderService is used for work with Orders
     */
    @Autowired
    private OrderService orderService;

    /**
     * OrderValidator is used to validate passed orderDto
     */
    @Autowired
    private OrderValidator orderValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(orderValidator);
    }

    /**
     * Method create new order
     *
     * @param userId        id of user
     * @param orderDto      passed OrderDto
     * @param bindingResult result of validation
     * @return OrderDto
     * @throws ValidationException                     if passed OdredDto is not valid
     * @throws CertificateNameNotExistServiceException if passed name of certificate is not exist in DB
     * @throws IdNotExistServiceException              if user with such id is not exist in DB
     */
    @PostMapping("/users/{userId}/orders")
    public OrderDto makeOrder(@PathVariable int userId, @Valid @RequestBody OrderDto orderDto,
                              @RequestParam(required = false, defaultValue = LOCALE_EN) String language,
                              BindingResult bindingResult) throws ValidationException, CertificateNameNotExistServiceException,
            IdNotExistServiceException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Name of certificate is not valid!");
        }
        User user = new User();
        user.setId(userId);
        orderDto.setUser(user);
        OrderDto orderDtoResult = orderService.makeOrder(orderDto, language);
        HATEOASBuilder.addLinksToOrder(orderDtoResult);
        return orderDtoResult;
    }

    /**
     * Method gets orders by user id
     *
     * @param userId id of user
     * @param page   number of page
     * @param size   number of entity on page
     * @return List of OrderDto
     * @throws IdNotExistServiceException if user with such id is not exist in DB
     * @throws PaginationException        if page number equals zero
     */
    @GetMapping("/users/{userId}/orders")
    public List<OrderDto> getOrdersByUserId(@PathVariable long userId,
                                            @RequestParam(required = true, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                            @RequestParam(required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                            @RequestParam(required = false, defaultValue = LOCALE_EN) String language)
            throws IdNotExistServiceException, PaginationException {
        List<OrderDto> orderDtoList = orderService.getOrdersByUserId(userId, page, size, language);
        HATEOASBuilder.addLinksToOrders(orderDtoList);
        return orderDtoList;
    }

    /**
     * Method gets order by id
     *
     * @param id id of order
     * @return OrderDto
     * @throws IdNotExistServiceException if order with such id is not exist in DB
     */
    @GetMapping("/orders/{id}")
    public OrderDto getOrdersById(@PathVariable long id, @RequestParam(required = false, defaultValue = LOCALE_EN)
            String language) throws IdNotExistServiceException {
        OrderDto orderDtoResult = orderService.getOrder(id, language);
        HATEOASBuilder.addLinksToOrder(orderDtoResult);
        return orderDtoResult;
    }
}

