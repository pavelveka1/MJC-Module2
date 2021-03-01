package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.exceptionhandler.ValidationException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.CertificateNameNotExistServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import com.epam.esm.validator.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final String DEFAULT_PAGE_SIZE = "1000";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private static final String KEY_VALIDATION="order_not_valid";
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
     * @param orderDto      passed OrderDto
     * @param bindingResult result of validation
     * @return OrderDto
     * @throws ValidationException                     if passed OdredDto is not valid
     * @throws CertificateNameNotExistServiceException if passed name of certificate is not exist in DB
     * @throws IdNotExistServiceException              if user with such id is not exist in DB
     */
    @PostMapping
    public OrderDto makeOrder( @Valid @RequestBody OrderDto orderDto,
                              BindingResult bindingResult) throws ValidationException, CertificateNameNotExistServiceException,
            IdNotExistServiceException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(KEY_VALIDATION);
        }
        OrderDto orderDtoResult = orderService.makeOrder(orderDto);
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
    @GetMapping("/users/{userId}")
    public List<OrderDto> getOrdersByUserId(@PathVariable long userId,
                                            @RequestParam( defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                            @RequestParam( defaultValue = DEFAULT_PAGE_SIZE) Integer size)
            throws IdNotExistServiceException, PaginationException {
        List<OrderDto> orderDtoList = orderService.getOrdersByUserId(userId, page, size);
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
    @GetMapping("/{id}")
    public OrderDto getOrdersById(@PathVariable long id) throws IdNotExistServiceException {
        OrderDto orderDtoResult = orderService.getOrder(id);
        HATEOASBuilder.addLinksToOrder(orderDtoResult);
        return orderDtoResult;
    }
}

