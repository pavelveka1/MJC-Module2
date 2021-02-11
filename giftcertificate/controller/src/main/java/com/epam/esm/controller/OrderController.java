package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.exceptionhandler.ValidationException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.CertificateNameNotExistServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
import com.epam.esm.validator.OrderValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/controller/api")
public class OrderController {

    private static final Logger logger = Logger.getLogger(GiftCertificateController.class);
    private static final String DEFAULT_PAGE_SIZE="1000";
    private static final String DEFAULT_PAGE_NUMBER="1";
    /**
     * OrderService is used for work with Orders
     */
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderValidator orderValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(orderValidator);
    }


    @PostMapping("/users/{userId}/orders")
    public OrderDto makeOrder(@PathVariable int userId, @Valid @RequestBody OrderDto orderDto, BindingResult bindingResult) throws ValidationException, CertificateNameNotExistServiceException, IdNotExistServiceException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Name of certificate is not valid!");
        }
        User user = new User();
        user.setId(userId);
        orderDto.setUser(user);
        OrderDto orderDtoResult = orderService.makeOrder(orderDto);
        orderDtoResult.add(linkTo(methodOn(OrderController.class).getOrdersById(orderDtoResult.getOrders_id())).withSelfRel());
        return orderDtoResult;
    }

    @GetMapping("/users/{userId}/orders")
    public List<OrderDto> getOrdersByUserId(@PathVariable long userId,  @RequestParam(required = true, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                            @RequestParam(required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size ) throws IdNotExistServiceException, PaginationException {
        List<OrderDto> orderDtoList = orderService.getOrdersByUserId(userId, page, size);
        for (OrderDto orderDto : orderDtoList) {
            orderDto.add(linkTo(methodOn(OrderController.class).getOrdersById(orderDto.getOrders_id())).withSelfRel());
        }
        return orderDtoList;
    }

    @GetMapping("/orders/{id}")
    public OrderDto getOrdersById(@PathVariable long id) throws IdNotExistServiceException {
        OrderDto orderDtoResult = orderService.getOrder(id);
        orderDtoResult.add(linkTo(methodOn(OrderController.class).getOrdersById(orderDtoResult.getOrders_id())).withSelfRel());
        return orderDtoResult;
    }
}

