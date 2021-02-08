package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.exceptionhandler.ValidationException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.CertificateNameNotExistServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
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
        return orderService.makeOrder(orderDto);
    }

    @GetMapping("/users/{userId}/orders")
    public List<OrderDto> getOrdersByUserId(@PathVariable long userId) throws IdNotExistServiceException {
        return orderService.getOrdersByUserId(userId);
    }

}

